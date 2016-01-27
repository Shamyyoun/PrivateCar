/*
 * Copyright (c) 2014-2015 Amberfog.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.privatecar.privatecar.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.utils.CountriesUtils;

public class CountryAdapter extends ArrayAdapter<String> {

    private LayoutInflater mLayoutInflater;
    private CountriesUtils countriesUtils = new CountriesUtils();

    public CountryAdapter(Context context) {
        super(context, 0);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_country_drop, parent, false);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.image);
            holder.mNameView = (TextView) convertView.findViewById(R.id.country_name);
            holder.mCodeView = (TextView) convertView.findViewById(R.id.country_code);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mNameView.setText(countriesUtils.getName(position));
        holder.mCodeView.setText(countriesUtils.getCode(position));
        holder.mImageView.setImageResource(countriesUtils.getFlagResourceId(convertView.getContext(), position));

        return convertView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_country, null);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        imageView.setImageResource(countriesUtils.getFlagResourceId(convertView.getContext(), position));

        TextView code = (TextView) convertView.findViewById(R.id.code);
        code.setText(countriesUtils.getCode(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return countriesUtils.getCountryNames().length;
    }

    private static class ViewHolder {
        public ImageView mImageView;
        public TextView mNameView;
        public TextView mCodeView;
    }
}

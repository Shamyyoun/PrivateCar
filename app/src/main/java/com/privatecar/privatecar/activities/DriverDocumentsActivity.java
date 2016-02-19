package com.privatecar.privatecar.activities;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.privatecar.privatecar.R;
import com.privatecar.privatecar.adapters.DriverDocumentsRVAdapter;

import java.util.ArrayList;

public class DriverDocumentsActivity extends BasicBackActivity {


    RecyclerView rvDocuments;
    ArrayList<String> documents = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_documents);

        fillDumpDocuments();

        rvDocuments = (RecyclerView) findViewById(R.id.rv_documents);
        rvDocuments.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
//        rvDocuments.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rvDocuments.setHasFixedSize(true);
        rvDocuments.setAdapter(new DriverDocumentsRVAdapter(this, documents));

    }

    private void fillDumpDocuments() {
        documents.add("https://pmcdeadline2.files.wordpress.com/2015/01/penelope-cruz.jpg?w=446&h=299&crop=1");
        documents.add("http://img2.timeinc.net/people/i/2007/database/penelopecruz/penelope_cruz300.jpg");
        documents.add("http://media2.onsugar.com/files/2013/12/17/689/n/1922398/d64f96e9d3e49da6_watermark-penelope-6.jpg");
        documents.add("http://cineuropa.org/imgCache/2014/01/21/1390305583526_0570x0365_1390305638250.jpg");
        documents.add("http://hbz.h-cdn.co/assets/cm/15/03/54bc53c66574e_-_hbz-penelope-cruz-0512-3-xln.jpg");
        documents.add("http://cdn02.cdn.justjared.com/wp-content/uploads/headlines/2015/04/cruz-joines-zoolander-sequel.jpg");
        documents.add("http://images4.fanpop.com/image/photos/19100000/Pen-lope-Cruz-photoshoot-HQ-penelope-cruz-19189797-1000-1255.jpg");
        documents.add("http://marieclaire.media.ipcdigital.co.uk/11116/00006bfd2/26b1_orh100000w440/penelope-cruz-garticle.jpg");
        documents.add("http://www.prothinspo.com/images/penelope-cruz-1_1_.jpg");
    }
}

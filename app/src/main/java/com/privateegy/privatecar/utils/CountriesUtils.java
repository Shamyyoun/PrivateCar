package com.privateegy.privatecar.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.util.Locale;

/**
 * Created by basim on 26/1/16.
 * A class that contains a list of countries and their ISOs and calling codes.
 */
public class CountriesUtils {

    String appLanguage = Utils.getAppLanguage();

    private String[] countryNames = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Caribbean Netherlands", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo (DRC)", "Congo (Republic)", "Cook Islands", "Costa Rica", "Côte d’Ivoire", "Croatia", "Cuba", "Curaçao", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Guiana", "French Polynesia", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Palestine", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Puerto Rico", "Qatar", "Réunion", "Romania", "Russia", "Rwanda", "Saint Barthélemy", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin", "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "São Tomé and Príncipe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Sint Maarten", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Korea", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "U.S. Virgin Islands", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis and Futuna", "Yemen", "Zambia", "Zimbabwe"};

    private String[] countryNamesAr = new String[]{"أفغانستان", "ألبانيا", "الجزائر", "ساموا الأمريكية", "أندورا", "أنغولا", "أنغيلا", "أنتيغوا وبربودا", "الأرجنتين", "أرمينيا", "أروبا", "أستراليا ", " النمسا ", " أذربيجان ", " الباهاما ", " البحرين ", " بنغلاديش ", " بربادوس ", " روسيا البيضاء ", " بلجيكا ", " بليز ", " بنين ", " برمودا ", " بوتان ", "بوليفيا", "البوسنة والهرسك", "بوتسوانا", "البرازيل", "إقليم المحيط الهندي البريطاني", "الجزر العذراء البريطانية", "بروناي", "بلغاريا", "بوركينا فاسو", "بوروندي", "كمبوديا ", " الكاميرون ", " كندا ", " الرأس الأخضر ", " البحر الكاريبي هولندا ", " جزر كايمان ", " جمهورية أفريقيا الوسطى ", " تشاد ", " شيلي ", " الصين ", " كولومبيا ", " جزر القمر ", "الكونغو (جمهورية الكونغو الديمقراطية)", "الكونغو (جمهورية)", "جزر كوك", "كوستاريكا", "كوت ديفوار", "كرواتيا", "كوبا", "كوراساو", "قبرص", "التشيكية الجمهورية ", " الدنمارك ", " جيبوتي ", " دومينيكا ", " جمهورية الدومينيكان ", " الإكوادور ", " مصر ", " السلفادور ", " غينيا الاستوائية ", " إريتريا ", " استونيا ", " إثيوبيا ", "جزر فوكلاند", "جزر فارو", "فيجي", "فنلندا", "فرنسا", "جويانا الفرنسية", "بولينيزيا الفرنسية", "الغابون", "غامبيا", "جورجيا", "ألمانيا", "غانا ", " جبل طارق ", " اليونان ", " غرينلاند ", " غرينادا ", " جوادلوب ", " غوام ", " غواتيمالا ", " غينيا ", " غينيا بيساو ", " غيانا ", " هايتي ", " هندوراس ", " هونغ كونغ ", " المجر ", " أيسلندا ", " الهند ", " إندونيسيا ", " إيران ", " العراق ", " ايرلندا ", " إسرائيل ", " إيطاليا ", " جامايكا ", " اليابان ", "الأردن", "كازاخستان", "كينيا", "كيريباس", "الكويت", "قيرغيزستان", "لاوس", "لاتفيا", "لبنان", "ليسوتو", "ليبيريا", "ليبيا", " ليختنشتاين ", " ليتوانيا ", " لوكسمبورغ ", " ماكاو ", " مقدونيا ", " مدغشقر ", " ملاوي ", " ماليزيا ", " جزر المالديف ", " مالي ", " مالطا ", " جزر مارشال ", " مارتينيك ", " موريتانيا ", " موريشيوس ", " المكسيك ", " ميكرونيزيا ", " مولدافيا ", " موناكو ", " منغوليا ", " الجبل الأسود ", " مونتسيرات ", " المغرب ", " موزمبيق ", " ميانمار ", "ناميبيا", "ناورو", "نيبال", "هولندا", "كاليدونيا الجديدة", "نيوزيلندا", "نيكاراغوا", "النيجر", "نيجيريا", "نيوي", "جزيرة نورفولك", "كوريا الشمالية ", " جزر ماريانا الشمالية ", " النرويج ", " عمان ", " باكستان ", " بالاو ", " فلسطين ", " بنما ", " بابوا غينيا الجديدة ", " باراغواي ", " بيرو ", " الفلبين ", "بولندا", "البرتغال", "بورتو ريكو", "قطر", "ريونيون", "رومانيا", "روسيا", "رواندا", "سانت بارتيليمي", "سانت هيلانة", "سانت كيتس ونيفيس", "سانت لوسيا", "سانت مارتن", "سان بيار وميكلون", "سانت فنسنت وجزر غرينادين", "ساموا", "سان مارينو", "ساو تومي وبرينسيبي", "المملكة العربية السعودية", "السنغال", "صربيا", "سيشيل", "سيراليون", "سنغافورة", "سانت مارتن", "سلوفاكيا", "سلوفينيا", "جزر سليمان", "الصومال", "جنوب أفريقيا", "كوريا الجنوبية", " جنوب السودان ", " اسبانيا ", " سري لانكا ", " السودان ", " سورينام ", " سوازيلاند ", " السويد ", " سويسرا ", " سوريا ", " تايوان ", " طاجيكستان ", " تنزانيا ", " تايلاند ", " تيمور-ليشتي ", " توغو ", " توكيلاو ", " تونغا ", " ترينيداد وتوباغو ", " تونس ", " تركيا ", " تركمانستان ", " جزر تركس وكايكوس ", " توفالو ", "لنا جزر العذراء ", " أوغندا ", " أوكرانيا ", " الإمارات العربية المتحدة ", " المملكة المتحدة ", " الولايات المتحدة الأمريكية ", " أوروغواي ", " أوزبكستان ", " فانواتو ", " الفاتيكان ", " فنزويلا ", " فيتنام ", " واليس وفوتونا ", " اليمن ", " زامبيا ", " زيمبابوي "};


    private String[] countryISOs = new String[]{"af", "al", "dz", "as", "ad", "ao", "ai", "ag", "ar", "am", "aw", "au", "at", "az", "bs", "bh", "bd", "bb", "by", "be", "bz", "bj", "bm", "bt", "bo", "ba", "bw", "br", "io", "vg", "bn", "bg", "bf", "bi", "kh", "cm", "ca", "cv", "bq", "ky", "cf", "td", "cl", "cn", "co", "km", "cd", "cg", "ck", "cr", "ci", "hr", "cu", "cw", "cy", "cz", "dk", "dj", "dm", "do", "ec", "eg", "sv", "gq", "er", "ee", "et", "fk", "fo", "fj", "fi", "fr", "gf", "pf", "ga", "gm", "ge", "de", "gh", "gi", "gr", "gl", "gd", "gp", "gu", "gt", "gn", "gw", "gy", "ht", "hn", "hk", "hu", "is", "in", "id", "ir", "iq", "ie", "il", "it", "jm", "jp", "jo", "kz", "ke", "ki", "kw", "kg", "la", "lv", "lb", "ls", "lr", "ly", "li", "lt", "lu", "mo", "mk", "mg", "mw", "my", "mv", "ml", "mt", "mh", "mq", "mr", "mu", "mx", "fm", "md", "mc", "mn", "me", "ms", "ma", "mz", "mm", "na", "nr", "np", "nl", "nc", "nz", "ni", "ne", "ng", "nu", "nf", "kp", "mp", "no", "om", "pk", "pw", "ps", "pa", "pg", "py", "pe", "ph", "pl", "pt", "pr", "qa", "re", "ro", "ru", "rw", "bl", "sh", "kn", "lc", "mf", "pm", "vc", "ws", "sm", "st", "sa", "sn", "rs", "sc", "sl", "sg", "sx", "sk", "si", "sb", "so", "za", "kr", "ss", "es", "lk", "sd", "sr", "sz", "se", "ch", "sy", "tw", "tj", "tz", "th", "tl", "tg", "tk", "to", "tt", "tn", "tr", "tm", "tc", "tv", "vi", "ug", "ua", "ae", "gb", "us", "uy", "uz", "vu", "va", "ve", "vn", "wf", "ye", "zm", "zw"};


    private String[] countryCodes = new String[]{"+93", "+355", "+213", "+1684", "+376", "+244", "+1264", "+1268", "+54", "+374", "+297", "+61", "+43", "+994", "+1242", "+973", "+880", "+1246", "+375", "+32", "+501", "+229", "+1441", "+975", "+591", "+387", "+267", "+55", "+246", "+1284", "+673", "+359", "+226", "+257", "+855", "+237", "+1", "+238", "+599", "+1345", "+236", "+235", "+56", "+86", "+57", "+269", "+243", "+242", "+682", "+506", "+225", "+385", "+53", "+599", "+357", "+420", "+45", "+253", "+1767", "+1", "+593", "+20", "+503", "+240", "+291", "+372", "+251", "+500", "+298", "+679", "+358", "+33", "+594", "+689", "+241", "+220", "+995", "+49", "+233", "+350", "+30", "+299", "+1473", "+590", "+1671", "+502", "+224", "+245", "+592", "+509", "+504", "+852", "+36", "+354", "+91", "+62", "+98", "+964", "+353", "+972", "+39", "+1876", "+81", "+962", "+7", "+254", "+686", "+965", "+996", "+856", "+371", "+961", "+266", "+231", "+218", "+423", "+370", "+352", "+853", "+389", "+261", "+265", "+60", "+960", "+223", "+356", "+692", "+596", "+222", "+230", "+52", "+691", "+373", "+377", "+976", "+382", "+1664", "+212", "+258", "+95", "+264", "+674", "+977", "+31", "+687", "+64", "+505", "+227", "+234", "+683", "+672", "+850", "+1670", "+47", "+968", "+92", "+680", "+970", "+507", "+675", "+595", "+51", "+63", "+48", "+351", "+1", "+974", "+262", "+40", "+7", "+250", "+590", "+290", "+1869", "+1758", "+590", "+508", "+1784", "+685", "+378", "+239", "+966", "+221", "+381", "+248", "+232", "+65", "+1721", "+421", "+386", "+677", "+252", "+27", "+82", "+211", "+34", "+94", "+249", "+597", "+268", "+46", "+41", "+963", "+886", "+992", "+255", "+66", "+670", "+228", "+690", "+676", "+1868", "+216", "+90", "+993", "+1649", "+688", "+1340", "+256", "+380", "+971", "+44", "+1", "+598", "+998", "+678", "+39", "+58", "+84", "+681", "+967", "+260", "+263"};

    public String[] getCountryNames() {
        switch (appLanguage) {
            case "ar":
                return countryNamesAr;
            default:
                return countryNames;
        }
    }


    public String[] getCountryISOs() {
        return countryISOs;
    }


    public String[] getCountryCodes() {
        return countryCodes;
    }

    public String getName(int position) {
        return getCountryNames()[position];
    }

    public String getISO(int position) {
        return getCountryISOs()[position];
    }

    public String getCode(int position) {
        return getCountryCodes()[position];
    }


    public int getFlagResourceId(Context ctx, int position) {
        return ctx.getResources().getIdentifier(String.format(Locale.ENGLISH, "f%03d", position), "drawable", ctx.getPackageName());

    }


    public Drawable getFlagDrawable(Context ctx, int position) {
        int id = ctx.getResources().getIdentifier(String.format("f%03d", position), "drawable", ctx.getPackageName());
        if (id == 0) {
            return null;
        }
        return ContextCompat.getDrawable(ctx, id);
    }


}

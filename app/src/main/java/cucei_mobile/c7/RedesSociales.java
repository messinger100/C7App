package cucei_mobile.c7;

/*----------------------------
C7 Jalisco for Android

Desarrollado por:

CuceiMobile Laboratorio de Dispositivos Móviles
Universidad de Guadalajara

Programadores:

Alexis Tomás Acevedo
Jorge Antonio Martínez Tejeda

Diseñadores:

Jonathan Gamaliel Arce Rivera

-----------------------------*/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TabHost;

public class RedesSociales extends Activity
{
    Funciones func = new Funciones();
    Context context = null;
    WebView wb, wb2, wb3, wb4, wb5;
    private FragmentTabHost tabHost;
    String red = "", plataforma = "";

    private class Navegador extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redes_sociales);
        if(func.esTablet(getApplicationContext()))
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Resources res = getResources();
        TabHost tabs = (TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();
        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");

        spec.setContent(R.id.tab1);
        spec.setIndicator("", getResources().getDrawable(R.drawable.tumblr));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("mitab2");

        spec.setContent(R.id.tab2);
        spec.setIndicator("", getResources().getDrawable(R.drawable.facebook));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("mitab3");

        spec.setContent(R.id.tab3);
        spec.setIndicator("", getResources().getDrawable(R.drawable.instagram));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("mitab4");

        spec.setContent(R.id.tab4);
        spec.setIndicator("", getResources().getDrawable(R.drawable.vine));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("mitab5");

        spec.setContent(R.id.tab5);
        spec.setIndicator("", getResources().getDrawable(R.drawable.youtube));
        tabs.addTab(spec);
        spec=tabs.newTabSpec("mitab6");

        Bundle bundle = getIntent().getExtras();
        plataforma = bundle.getString("plataforma");
        switch(bundle.getString("red"))
        {
            case "twitter":
                tabs.setCurrentTab(0);
                break;
            case "facebook":
                tabs.setCurrentTab(1);
                break;
            case "instagram":
                tabs.setCurrentTab(2);
                break;
            case "vine":
                tabs.setCurrentTab(3);
                break;
            case "youtube":
                tabs.setCurrentTab(4);
                break;
        }

        tabs.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {
            public void onTabChanged(String tabId)
            {
                Log.i("AndroidTabsDemo", "Pulsada pestaña: " + tabId);
            }
        });

        webFacebook();
        webTwitter();
        webInstagram();
        webVine();
        webYoutube();
    }

    private void webFacebook()
    {
        wb=(WebView)findViewById(R.id.webFacebook);
        wb.getSettings().setJavaScriptEnabled(true);
        wb.getSettings().setLoadWithOverviewMode(true);
        wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setBuiltInZoomControls(true);
        wb.getSettings().setPluginState(WebSettings.PluginState.ON);
        wb.setWebViewClient(new Navegador());
        wb.loadUrl("http://www.facebook.com/c7jalisco");
    }

    private void webTwitter()
    {
        wb2=(WebView)findViewById(R.id.webTwitter);
        wb2.getSettings().setJavaScriptEnabled(true);
        wb2.getSettings().setLoadWithOverviewMode(true);
        wb2.getSettings().setUseWideViewPort(true);
        wb2.getSettings().setBuiltInZoomControls(true);
        wb2.getSettings().setPluginState(WebSettings.PluginState.ON);
        wb2.setWebViewClient(new Navegador());
        wb2.loadUrl("https://twitter.com/#!/c7jalisco");
    }

    private void webInstagram()
    {
        wb3=(WebView)findViewById(R.id.webInstagram);
        wb3.getSettings().setJavaScriptEnabled(true);
        wb3.getSettings().setLoadWithOverviewMode(true);
        wb3.getSettings().setUseWideViewPort(true);
        wb3.getSettings().setBuiltInZoomControls(true);
        wb3.getSettings().setPluginState(WebSettings.PluginState.ON);
        wb3.setWebViewClient(new Navegador());
        wb3.loadUrl("https://instagram.com/c7jalisco");
    }

    private void webVine()
    {
        wb4=(WebView)findViewById(R.id.webVine);
        wb4.getSettings().setJavaScriptEnabled(true);
        wb4.getSettings().setLoadWithOverviewMode(true);
        wb4.getSettings().setUseWideViewPort(true);
        wb4.getSettings().setBuiltInZoomControls(true);
        wb4.getSettings().setPluginState(WebSettings.PluginState.ON);
        wb4.setWebViewClient(new Navegador());
        wb4.loadUrl("https://vine.co/u/1165496545735548928");
    }

    private void webYoutube()
    {
        wb5=(WebView)findViewById(R.id.webYoutube);
        wb5.getSettings().setJavaScriptEnabled(true);
        wb5.getSettings().setLoadWithOverviewMode(true);
        wb5.getSettings().setUseWideViewPort(true);
        wb5.getSettings().setBuiltInZoomControls(true);
        wb5.getSettings().setPluginState(WebSettings.PluginState.ON);
        wb5.setWebViewClient(new Navegador());
        wb5.loadUrl("https://www.youtube.com/user/CsieteTVJalisco");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            switch(plataforma)
            {
                case "noticias":
                    Intent intentNoticias = new Intent(getApplicationContext(), Menu_Noticias.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentNoticias);
                    return true;
                case "cultura":
                    Intent intentCultura = new Intent(getApplicationContext(), Menu_Cultura.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentCultura);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
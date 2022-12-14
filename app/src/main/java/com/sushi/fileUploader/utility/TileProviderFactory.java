package com.sushi.fileUploader.utility;

import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class TileProviderFactory {

    public static WMSTileProvider getOsgeoWmsTileProvider(String BaseUrl, String service, String version,
                                                          String request, String layer, String srs, String format, String trans) {

        final String WMS_FORMAT_STRING =
                BaseUrl + "?service=" + service  +
                        "&version=" + version + "&request=" + request +
                        "&layers=" + layer + "&bbox=%f,%f,%f,%f" +
                        "&width=256" + "&height=256" +
                        "&srs=" + srs +  // NB This is important, other SRS's won't work.
                        "&format=" + format + "&transparent=" + trans;

        WMSTileProvider tileProvider = new WMSTileProvider(256, 256) {

            @Override
            public synchronized URL getTileUrl(int x, int y, int zoom) {
                double[] bbox = getBoundingBox(x, y, zoom);
                String s = String.format(Locale.US, WMS_FORMAT_STRING, bbox[MINX],
                        bbox[MINY], bbox[MAXX], bbox[MAXY]);
                Log.d("WMSDEMO", s);
                URL url = null;
                try {
                    url = new URL(s);
                } catch (MalformedURLException e) {
                    throw new AssertionError(e);
                }
                return url;
            }
        };
        return tileProvider;
    }
}

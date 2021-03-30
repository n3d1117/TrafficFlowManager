package it.ned.TrafficFlowManager.utils;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.imageio.GeoToolsWriteParams;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriteParams;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.map.FeatureLayer;
import org.geotools.map.MapContent;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

public class GeoTiffExtractor {
/*
    public static byte[] createGeotiffImageForNumericalValues(SimpleFeatureCollection featureCollection, String attributeName, String feaureUniqueIdentifier) throws IOException {
        MapContent map = new MapContent();
        map.setTitle("GeoTiff");

        featureCollection.features();
        SimpleFeatureIterator iterator = featureCollection.features();
        SimpleFeature feature;
        Collection<Property> properties;
        float valueDivided;
        double highestValue = extractHighestValue(iterator, featureCollection, attributeName); //this is for creating the colour spectrum, used later to create the style
        Object val;
        iterator = featureCollection.features();
        Color fillerColor;
        FeatureLayer featureLayer;
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        try {
            while (iterator.hasNext()) {
                feature = iterator.next();
                SimpleFeatureType type = feature.getType();
                properties = feature.getProperties();
                for (Property property : properties) {
                    String propName = property.getName().getLocalPart();
                    if (propName.equals(attributeName)) {
                        val = property.getValue();
                        if (val == null) {
                            continue;
                        }
                        valueDivided = (float) ((double) val / highestValue);
                        fillerColor = new Color(0, 1, 0, valueDivided);
                        featureLayer = new FeatureLayer(featureCollection, SLD.createPolygonStyle(new Color(1, 1, 1),
                                fillerColor, (float) 0.5));
                        Filter fil = ff.equals(ff.property(feaureUniqueIdentifier),
                                ff.literal(feature.getProperty(feaureUniqueIdentifier).getValue()));
                        featureLayer.setQuery(new Query(type.getName().getLocalPart(), fil));
                        map.addLayer(featureLayer);
                    }
                }
            }
        } finally {
            iterator.close();
        }

        Rectangle screen = new Rectangle(1500, 1500);
        StreamingRenderer renderer = new StreamingRenderer();
        ReferencedEnvelope maxBounds = map.getMaxBounds(); // <-- this the 1st point that slows everything down
        renderer.setMapContent(map);

        BufferedImage image = new BufferedImage(screen.width, screen.height, BufferedImage.TYPE_INT_ARGB);
        map.getViewport().setBounds(maxBounds);

        Graphics2D destGraphics = image.createGraphics();
        renderer.paint(destGraphics, screen, maxBounds); // <-- this the 2nd

        Hints hints = GeoTools.getDefaultHints();
        GridCoverage2D coverage = new GridCoverageFactory(hints)
                .create("GeoTiff", image, featureCollection.getBounds());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        GeoTiffWriter writer = new GeoTiffWriter(byteArrayOutputStream);
        GeoTiffWriteParams params = new GeoTiffWriteParams();
        ParameterValue<GeoToolsWriteParams> value = GeoTiffFormat.GEOTOOLS_WRITE_PARAMS.createValue();
        value.setValue(params);
        writer.write(coverage, new GeneralParameterValue[] { value });
        writer.dispose();

        return byteArrayOutputStream.toByteArray();
    }

    public static double extractHighestValue(SimpleFeatureIterator iterator, SimpleFeatureCollection c, String attributeName) {
        SimpleFeatureType schema = c.getSchema();
        SimpleFeature feature;
        Collection<Property> properties;
        double highestValue = 0;
        try {
            while (iterator.hasNext()) {
                feature = iterator.next();
                properties = feature.getProperties();
                for (Property property : properties) {
                    String propName = property.getName().getLocalPart();
                    if (propName.equals(attributeName)) {
                        Object val = property.getValue();
                        if (val == null) {
                            continue;
                        }
                        if ((double) val > highestValue) {
                            highestValue = (double) val;
                        }
                    }
                }
            }
        } finally {
            iterator.close();
        }
        return highestValue;
    }
*/
    public static void main(String[] args) throws IOException {
        /*URL url =  new File("d:\\gis\\tmp\\32 27 25 w4.shp").toURI().toURL();
        ShapefileDataStore ds = new ShapefileDataStore(url);
        FeatureSource fs = ds.getFeatureSource("32%2027%2025%20w4");

        GridCoverage2D raster = VectorToRasterProcess.process(fs.getFeatures(),
                "ID", new Dimension(400, 400), fs.getBounds(),"test", null);

        ImageWorker imageWorker = new ImageWorker(raster.getRenderedImage());
        imageWorker = imageWorker.rescaleToBytes();
        imageWorker.makeColorTransparent(Color.BLACK);
        imageWorker.forceComponentColorModel();

        raster = new GridCoverageFactory().create("one", imageWorker.getRenderedImage(),  fs.getBounds());

        GeoTiffWriter writer = new GeoTiffWriter(output);
        GeoTiffWriteParams params = new GeoTiffWriteParams();
        ParameterValue<GeoToolsWriteParams> value = GeoTiffFormat.GEOTOOLS_WRITE_PARAMS.createValue();
        value.setValue(params);
        writer.write(raster, new GeneralParameterValue[]{value});
        writer.dispose();*/
    }
}

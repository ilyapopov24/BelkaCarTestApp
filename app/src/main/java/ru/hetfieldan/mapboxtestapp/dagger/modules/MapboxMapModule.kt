package ru.hetfieldan.mapboxtestapp.dagger.modules

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import com.mapbox.geojson.FeatureCollection
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.maps.MapboxMapOptions
import com.mapbox.mapboxsdk.style.expressions.Expression
import com.mapbox.mapboxsdk.style.layers.LineLayer
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import dagger.Module
import dagger.Provides
import ru.hetfieldan.mapboxtestapp.domain.*

@Module
class MapboxMapModule {
    @Provides
    fun provideMainOptions(activity: AppCompatActivity): MapboxMapOptions {
        return MapboxMapOptions.createFromAttributes(activity, null).apply {
            camera(
                CameraPosition.Builder()
                    .target(LatLng(37.79388685, 55.723999516666666))
                    .zoom(1.0)
                    .build()
            )
        }
    }

    @Provides
    fun provideLocationOptions(activity: AppCompatActivity): LocationComponentOptions {
        return LocationComponentOptions.builder(activity)
            .trackingGesturesManagement(true)
            .build()
    }

    @Provides
    fun provideMainlLayer(): SymbolLayer {
        return SymbolLayer(MAIN_LAYER_ID, MAIN_SOURCE_ID)
            .withProperties(
                PropertyFactory.iconRotate(Expression.get(PROPERTY_ANGLE)),
                PropertyFactory.iconImage(Expression.get(PROPERTY_COLOR))
            )
    }

    @Provides
    fun provideDirectionsLayer(): LineLayer {
        return LineLayer(
            DIRECTIONS_LAYER_ID, DIRECTIONS_SOURCE_ID
        ).withProperties(
            PropertyFactory.lineWidth(4.5f),
            PropertyFactory.lineColor(Color.BLACK),
            PropertyFactory.lineTranslate(arrayOf(0f, 4f)),
            PropertyFactory.lineDasharray(arrayOf(1.2f, 1.2f))
        )
    }

    @Provides
    fun provideDefaultDirectionsSource(): GeoJsonSource {
        return GeoJsonSource(DIRECTIONS_SOURCE_ID, FeatureCollection.fromFeatures(arrayOf()))
    }
}

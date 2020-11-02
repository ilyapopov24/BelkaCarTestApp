package ru.hetfieldan.mapboxtestapp.presentation.ui

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.CircularProgressDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import ru.hetfieldan.mapboxtestapp.R
import ru.hetfieldan.mapboxtestapp.domain.CarItem
import ru.hetfieldan.mapboxtestapp.makeStringFromLabelAndValue
import ru.hetfieldan.mapboxtestapp.mylog
import ru.hetfieldan.mapboxtestapp.putArgs
import kotlin.random.Random


class SymbolLayerBottomSheet: BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_fragment_symbol_layer, container, false)

        val carJSON = arguments?.getString(EXTRA_CAR_JSON) ?: ""
        val car = Gson().fromJson(carJSON, CarItem::class.java)

        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val numberTextView = view.findViewById<TextView>(R.id.numberTextView)
        val fuelTextView = view.findViewById<TextView>(R.id.fuelTextView)
        val imageView = view.findViewById<ImageView>(R.id.imageView)

        with (car) {
            nameTextView.text = name
            numberTextView.text = makeStringFromLabelAndValue("Номер", plateNumber)
            fuelTextView.text = makeStringFromLabelAndValue("Топливо", fuelPercentage.toString())

            Glide.with(this@SymbolLayerBottomSheet)
                .load("https://picsum.photos/300/200?fakeParam=${Random.nextInt(1000000)}")
                .into(imageView)
        }

        return view
    }

    companion object {

        const val EXTRA_CAR_JSON = "EXTRA_CAR_JSON"

        fun newInstance(car: String): SymbolLayerBottomSheet {
            return SymbolLayerBottomSheet().putArgs {
                putString(EXTRA_CAR_JSON, car)
            }
        }
    }
}

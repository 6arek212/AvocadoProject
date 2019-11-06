package com.example.testavocado.ccc

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.testavocado.R
import com.example.testavocado.Utils.TimeMethods

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    Log.d("BindingAdapter", "${imgUrl}")

        Glide.with(imgView.context)
                .load(imgUrl)
                .centerCrop()
                .apply(
                        RequestOptions()
                                .placeholder(R.drawable.loading_animation)
                                .error(R.drawable.profile_ic)
                )
                .into(imgView)



}

@BindingAdapter("time")
fun time(view: TextView, time: String?) {
    time?.let {
        view.text = TimeMethods.convertDateTimeFormat2(time)
    }
}


@BindingAdapter("progressBarState")
fun time(view: ProgressBar, state: Boolean?) {
    state?.let {
        when(it){
            true->view.visibility= View.VISIBLE
            false->view.visibility=View.GONE
        }
    }
}
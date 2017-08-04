
package com.wingsofts.gankclient.ui

import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gkzxhn.gank_kotlin.R

/**
 * Created by wing on 11/24/16.
 */

  @BindingAdapter("load_image")
  fun loadImage(imageView: ImageView, url: String?) =
      Glide.with(imageView.context).load(url)
              .asBitmap()
              .format(DecodeFormat.PREFER_ARGB_8888)
              .diskCacheStrategy(DiskCacheStrategy.ALL)
              .centerCrop()
              .placeholder(R.drawable.ic_image_loading)
              .error(R.drawable.ic_empty_picture)
              .into(imageView)




  @BindingAdapter("load_asset")
  fun loadAsset(imageView: ImageView, id:Int) =
          Glide.with(imageView.context).load(id).into(imageView)

  @BindingAdapter("load_vector")
  fun loadVector(imageView: ImageView, id:Int) =
          imageView.setImageResource(id)


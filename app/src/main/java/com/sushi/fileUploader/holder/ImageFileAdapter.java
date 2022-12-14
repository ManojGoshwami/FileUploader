package com.sushi.fileUploader.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sushi.fileUploader.R;
import com.sushi.fileUploader.common.Utility;
import com.sushi.fileUploader.dbHelper.ImageFile_Helper;
import com.sushi.fileUploader.models.ImageFileModel;

import java.util.ArrayList;

public class ImageFileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<ImageFileModel> imageFileModels;
    private ImageFileModel imageFileModel;
    private Context context;

    public ImageFileAdapter(ArrayList<ImageFileModel> imageFileModels, Context context) {
        this.imageFileModels = imageFileModels;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);
        return new VHItem(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof VHItem) {
            imageFileModel = getItem(position);
           /* if (file.exists()) {
                new ImageAsync(file, imageFileModel, false, accountNo, new PhotoCompressedListener() {
                    @Override
                    public void compressedPhoto(String path) {
                        Log.e("Main Activity", "path : " + path);
                        UserBillModel userBillModel = User_Bill_Helper.getByAccountNo(accountNo, MyApplication.getInstance());
                        if (imageFileModel != null && imageFileModel.getIMAGE_NAME_PATH() != null) {
                            ((VHItem) holder).imgFile.setImageBitmap(Utility.getBitmapByStringImage(imageFileModel.getIMAGE_NAME_PATH()));
                        }
                    }
                }).execute();
            }*/
            if (imageFileModel != null && imageFileModel.getIMAGE_NAME_PATH() != null) {
                Glide.with(context.getApplicationContext())
                        .load(imageFileModel.getIMAGE_NAME_PATH())
                        .into(((VHItem) holder).imgFile);
//                ((VHItem) holder).imgFile.setImageBitmap(Utility.getBitmapByStringImage(imageFileModel.getIMAGE_NAME_PATH()));
            }


            ((VHItem) holder).itemView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageFileModel info = getItem(holder.getAdapterPosition());
                    if (info != null) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Confirmation")
                                    .setMessage("Are you sure to delete this image?")
                                    .setCancelable(false)
                                    .setPositiveButton("Okay", (dialog, which) -> {
                                        ImageFile_Helper.getInstance().DeleteById(Integer.parseInt(info.getID()), context);
                                        imageFileModels.remove(holder.getAdapterPosition());
                                        notifyDataSetChanged();
                                    })
                                    .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
                                    .show();

                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (imageFileModels.size() <= 0)
            return 0;
        else
            return imageFileModels.size();
    }

    public ArrayList<ImageFileModel> getItems() {
        return imageFileModels;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public ImageFileModel getItem(int position) {
        return imageFileModels.get(position);
    }

    private class VHItem extends RecyclerView.ViewHolder {

        private View itemView1;
        private ImageView imgFile;

        private VHItem(View itemView) {
            super(itemView);
            imgFile = (ImageView) itemView.findViewById(R.id.imgFile);
            itemView1 = itemView;
        }

        private void removeAt(int position) {
            imageFileModels.remove(position);
            notifyDataSetChanged();
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, expenseViewModels.size());
        }
    }
}

/**
 *
 * Copyright 2014 IOTEK. All rights reserved.
 * PhotoAlbumFunction.java
 *
 */
package com.willchun.library.demo.function;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.androidquery.AQuery;
import com.androidquery.util.AQUtility;
import com.willchun.library.base.AndActivity;
import com.willchun.library.base.AndAdapter;
import com.willchun.library.base.AndQuery;
import com.willchun.library.demo.R;
import com.willchun.library.utils.LogUtils;
import com.willchun.library.utils.UIUtils;

/**
 *@author willchun (277143980@qq.com)
 *@date 2014-3-5
 */
public class PhotoAlbumDirFunctionActivity extends AndActivity {

    private final String IMG_JPG="image/jpg";
    private final String IMG_JPEG="image/jpeg";
    private final String IMG_PNG="image/png";
    
    private AndAdapter<PhotoDirInfo> mAdapter;
    public static String DATA_KEY = "data_key";
    
    @Override
    protected void onCreate(Bundle savedState) {
        // TODO Auto-generated method stub
        super.onCreate(savedState);
        setContentView(R.layout.willchun_lib_activity_photo_album_dir);
        
        mAdapter = new AndAdapter<PhotoDirInfo>(this, R.layout.willchun_lib_item_photo_album_dir_list) {

            @Override
            protected void update(PhotoDirInfo item, AndQuery aq, int position) {
                // TODO Auto-generated method stub
            }

            @Override
            protected void update(PhotoDirInfo item, int position, AndQuery aq, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                super.update(item, position, aq, convertView, parent);
                aq.id(R.id.willchun_lib_item_photo_album_dir_list_title_tv).text(item.getDirName());
                aq.id(R.id.willchun_lib_item_photo_album_dir_list_content_tv).text(item.getPicCount() + "张");
                String url = item.getFirstPicPath();
                Bitmap placeholder = aq.getCachedImage(R.drawable.willchun_lib_photo_thumb_bg);
                if(aq.shouldDelay(position, convertView, parent, url)){
                    aq.id(R.id.willchun_lib_item_photo_album_dir_list_iv).image(placeholder);
                }else{
                    aq.id(R.id.willchun_lib_item_photo_album_dir_list_iv).image(url, true, true, UIUtils.getInstance(getWindowManager()).dip2Px(75), 0, placeholder, 0);
                }
            }
            
            

        };
        mAdapter.addAll(getImageDir(this));
        aq.id(R.id.willchun_lib_photo_album_dir_list_lv).adapter(mAdapter);
        
        aq.id(R.id.willchun_lib_photo_album_dir_list_lv).getListView().setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getBaseContext(), PhotoAlbumPicFunctionActivity.class);
                intent.putExtra("key", mAdapter.getItems().get(arg2).getDirId());
                startActivityForResult(intent, 101);
            }
        });
        
    }
    
    /**
     * 获取照片的目录
     * @param context
     * @return
     */
    private synchronized ArrayList<PhotoDirInfo> getImageDir(Context context) {
        ArrayList<PhotoDirInfo> list = null ;
        PhotoDirInfo dirInfo=null;
        ContentResolver mResolver = context.getContentResolver();
        String[] IMAGE_PROJECTION=new String[]{ImageColumns.BUCKET_ID,ImageColumns.BUCKET_DISPLAY_NAME,ImageColumns.DATA,"COUNT(*) AS "+ImageColumns.DATA};
        
        String selection=" 1=1 AND "+ImageColumns.MIME_TYPE+" IN (?,?,?)) GROUP BY ("+Images.ImageColumns.BUCKET_ID+") ORDER BY ("+Images.ImageColumns.BUCKET_DISPLAY_NAME;
        
        String[] selectionArgs=new String[] {IMG_JPG,IMG_JPEG,IMG_PNG};
//      String selection=ImageColumns.MIME_TYPE+" IN ("+IMG_JPG+","+IMG_PNG+")) GROUP BY ("+ImageColumns.BUCKET_ID+") ORDER BY ("+Images.ImageColumns.BUCKET_DISPLAY_NAME;
        
        Cursor cursor = mResolver.query(Images.Media.EXTERNAL_CONTENT_URI,IMAGE_PROJECTION,selection,selectionArgs,null);
        if(null!=cursor){
            if(cursor.getCount()>0){
                list=new ArrayList<PhotoDirInfo>();
                while(cursor.moveToNext()){
                    dirInfo=new PhotoDirInfo();
                    dirInfo.setDirId(cursor.getString(0));
                    dirInfo.setDirName(cursor.getString(1));
                    dirInfo.setFirstPicPath(cursor.getString(2));
                    dirInfo.setPicCount(cursor.getInt(3));
                    dirInfo.setUserOtherPicSoft(false);
                    list.add(dirInfo);
                }
            }
            cursor.close();
        }
        
        return list;
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if(arg1 == RESULT_OK && arg0 == 101){
                setResult(RESULT_OK, arg2);
                finish();
        }
    }
    
    
    
}

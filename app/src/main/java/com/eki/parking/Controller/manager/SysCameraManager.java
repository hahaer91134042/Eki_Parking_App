package com.eki.parking.Controller.manager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;


import com.eki.parking.App;
import com.eki.parking.AppConfig;
import com.eki.parking.AppRequestCode;
import com.eki.parking.Controller.impl.ICameraFileSet;
import com.eki.parking.Controller.tools.ForegroundCallbacks;
import com.hill.devlibs.activity.LibBaseActivity;
import com.hill.devlibs.time.DateTime;
import com.hill.devlibs.tools.Log;
import com.hill.devlibs.util.BitmapUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Hill on 2017/10/13.
 */

public class SysCameraManager extends BaseManager
                              implements ForegroundCallbacks.LifeCycleListener,
                                         LibBaseActivity.OnResultBack {
    private Activity activity;
    private App app;
//    private static Uri tempFileUri;

    private SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
    public static File
            appCameraDir,
            appCameraTempDir,
            appPictureDir;
//    private File saveFile;
    private CameraListenerControll listenerControl=new CameraListenerControll();
//    private String
//            picKey="img";
//    private String picName="";
//    public int defaultPicture= R.drawable.none_img;

    private final static int ImgCompressQuality=30;
//    private final static int RESIZE=4;

//    private int scaleX=480,scaleY=480;//預設不要1 會看不到


//    private HashMap<String,String> imgMap;

//    class RequestCode{
//        public final static int PHOTO = 1;
//        public final static int CAMERA = 2;
//    }
    public interface CameraResultListener{
        void onPicture(@NonNull File pic);
        void onPictureError();
    }
    public static abstract class DefaultPhotoSet extends ICameraFileSet {
        @NotNull
        @Override
        public File getSaveDir() {
            return appPictureDir;
        }
    }

    public SysCameraManager(App context){
        super(context);
        app=context;

//        ForegroundCallbacks.getInstance(app).setForegroundListener(this);//要自動抓目前使用的activity
        initPictureDir();
    }

    // TODO: 2017/10/19 -------set up camera--------
    public SysCameraManager formContext(Activity context) {
        this.activity=context;
        return this;
    }

//    public SysCameraManager setPicKey(String picKey){
//        this.picKey=picKey;
////        loadImg();
//        return this;
//    }
//    public String getPicKey(){
//        return picKey;
//    }

//    public void replaceImgKey(String newPicKey) {
//        imgMap.remove(picKey);
//        imgMap.put(newPicKey,picName);
////        getApp().putRoomImg(imgMap);
//        picKey=newPicKey;
//    }

    public void clear(){
        activity=null;
//        picKey="";
//        picName="";
//        imgMap=null;
    }
//    private void loadImg(){
//        imgMap=getApp().getRoomImgMap();
//        if (imgMap.get(picKey)!=null){
//            picName=imgMap.get(picKey);
//        }
//    }
//    public void setPhotoScale(int x,int y){
//        scaleX=x;
//        scaleY=y;
//    }



    private void initPictureDir() {

        String inAppImgPath;
        String cameraPath;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            inAppImgPath=app.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath();
            cameraPath=app.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/"+AppConfig.cameraDir;

        }else {
            inAppImgPath = Environment.getDataDirectory()+"/data/"+getPackageName()+"/picture";
            cameraPath = Environment.getExternalStorageDirectory().getPath() + "/" + Environment.DIRECTORY_DCIM + "/"+ AppConfig.cameraDir;
        }



        Log.w("--picture path-- inAppPath->"+inAppImgPath);

        appPictureDir=new File(inAppImgPath);
        if (!appPictureDir.exists()){
            appPictureDir.mkdir();
        }




        Log.i("--picture path-- cameraPath->"+cameraPath);

        appCameraDir = new File(cameraPath);
        if (!appCameraDir.exists()) {
            appCameraDir.mkdir();
        }

        appCameraTempDir=new File(cameraPath+"/temp");
        if(!appCameraTempDir.exists()){
            appCameraTempDir.mkdir();
        }

//        appPictureDir=app.getAppPictureDir();
//        cameraDir =app.getCameraDir();
//        appCameraTempDir=app.getCameraTempDir();


        Log.d("資料夾 exist", String.valueOf(appCameraDir.exists()));
        Log.d("dir path", appCameraDir.getAbsolutePath());
    }

    // todo --------set up camera------------------
//    public File getLastTempPicture(){
//        if (appCameraDir.listFiles().length<1){
//            return null;
//        }
//
//        return appCameraDir.listFiles()[appCameraDir.listFiles().length-1];
//    }

//    public void deletPictureInApp(){
//        if (isPictuerExist()){
//            return;
//        }
//        for (File picFile:
//             appPictureDir.listFiles()) {
//            if (picFile.getName().equals(cameraFileSet.getFileName())){
//                picFile.delete();
//            }
//        }
//    }
//    public void deletRoomImgData(){
//        imgMap.remove(picKey);
//        getApp().putRoomImg(imgMap);
//    }
//    public void deletTempPicture(){
//        Log.i("picture file num", appCameraDir.listFiles().length+"");
//        File[] fileList= appCameraDir.listFiles();
//
//        for (int i = 0; i <fileList.length ; i++) {
////            Log.w("File name",fileList[i].getName());
////            Log.w("File getAbsolutePath",fileList[i].getAbsolutePath());
////            Log.w("File getPath",fileList[i].getPath());
////            Log.w("File isDirectory",fileList[i].isDirectory()+"");
////            Log.w("File canRead",fileList[i].canRead()+"");
////            Log.w("File canWrite",fileList[i].canWrite()+"");
//
//            fileList[i].getAbsoluteFile().delete();
//
//        }
//    }

//    public boolean isPictuerExist(){
////        return picName.isEmpty();
//        return StringUtil.isEmptyString(cameraFileSet.getFileName());
//    }

//    private File getPictureFromKey(){

////        if (imgMap.get(picKey)==null
////                ||isPictuerExist()){// TODO: 2017/10/19 紀錄裡面沒有相片
////            return null;
////        }
//        if (isPictuerExist()){// TODO: 2017/10/19 紀錄裡面沒有相片
//            return null;
//        }
//
//
//        for (File imgFile:
//             appPictureDir.listFiles()) {
//            if (imgFile.getName().equals(cameraFileSet.getFileName())){// TODO: 2017/10/19 有找到相對應的圖片
//                return imgFile;
//            }
//        }
//        return null;
//    }

//    public<V extends ImageView> void loadPictureWithView(V view){
//
//        if (getPictureFromKey()!=null){
//            Picasso.with(view.getContext())
//                    .load(getPictureFromKey())
//                    .resize(scaleX,scaleY)
//                    .into(view);
//
//        }else{
//            Picasso.with(view.getContext())
//                    .load(defaultPicture)
//                    .resize(scaleX,scaleY)
//                    .into(view);
//        }
//    }

//    private File getSelectPicture(){
//        return getPictureFromKey();
//    }

    public void addCameraListenerFrom(CameraResultListener listener){
        Log.i("--SysCamera-- add cameraListener->"+listener);
        listenerControl.addListener(listener);
    }
    public void removeCameraListener(CameraResultListener listener){
        listenerControl.removeListener(listener);
    }

//    public void startPhotoFrom(BaseFragment frag) {
//    }
//
//    public void startCameraFrom(BaseFragment frag) {
//
//    }

    public void startPhoto(){
        startPhoto(sf.format(Calendar.getInstance().getTime()));
    }

    public void startPhoto(@NonNull ICameraFileSet set){
        cameraFileSet=set;
        toUserPhoto();
    }
    private void toUserPhoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(intent, AppRequestCode.PHOTO);
    }
    public void startPhoto(String fileName){
        cameraFileSet=new DefaultPhotoSet() {
            @NotNull
            @Override
            public String getFileName() {
                return fileName;
            }
        };
        toUserPhoto();
    }

    private ICameraFileSet cameraFileSet;

    public void startCamera(ICameraFileSet set){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);



        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            cameraFileSet=set;

//            saveFile = set.getTempFile();

            Log.e("--startCamera--  activity->"+activity);
            Log.d("--startCamera--  packagename->"+getPackageName());
//            Log.i("saveFile path"+saveFile.getPath());

            Uri fileUri=set.getTempFileUri();


            Log.d("--startCamera--  package->"+takePictureIntent.resolveActivity(activity.getPackageManager()));
            Log.d("--startCamera--  tempFileUri", String.valueOf(fileUri));
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            activity.startActivityForResult(takePictureIntent, AppRequestCode.CAMERA);
        }else {
            if (listenerControl.have()){
                listenerControl.onPictureError();
            }
        }
    }
//    public void startCamera(){
//
////        //開啟相機功能，並將拍照後的圖片存入SD卡相片集內，須由startActivityForResult且
//////        帶入
//////        requestCode進行呼叫，原因為拍照完畢後返回程式後則呼叫onActivityResult
////        ContentValues ratio = new ContentValues();
////        ratio.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
////        Uri uri= activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
////                ratio);
////        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
////        activity.startActivityForResult(intent, CAMERA);
//
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
//            // TODO: 2017/10/16 這邊等於直接將存檔路徑導向
//            // filename = cameraDir.getPath() + File.separator
//            // + "OP_GET_" + getRequest_id() + ".jpg";
//
//            String filename = appCameraTempDir.getPath() + File.separator
//                    + sf.format(new Date()) + ".jpg";
//            saveFile = new File(filename);
//
////            Log.e("activity->"+activity);
////            Log.d("packagename->"+getPackageName());
////            Log.i("saveFile path"+saveFile.getPath());
//
//            if (Build.VERSION.SDK_INT >= 24) {
//                tempFileUri = FileProvider.getUriForFile(activity, getPackageName()+".fileprovider", saveFile);
//            } else {
//                tempFileUri = Uri.fromFile(saveFile);
//            }
//            Log.d("tempFileUri", String.valueOf(tempFileUri));
//            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
//            activity.startActivityForResult(takePictureIntent, Request.Camera.Code);
//        }else {
//            if (listenerControl.have()){
//                listenerControl.onPictureError();
//            }
//        }
//    }

    @Override
    public List<Integer> requestCodes() {
        ArrayList<Integer> list=new ArrayList<>();
        list.add(AppRequestCode.CAMERA);
        list.add(AppRequestCode.PHOTO);
        return list;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //去掉非Camera所使用的request code
//        if (!(requestCode==AppRequestCode.CAMERA||requestCode==AppRequestCode.PHOTO)){
//            return;
//        }

        Log.e("sysCamera get result code->"+resultCode+" requestCode->"+requestCode+" data->"+data);
        if (resultCode != activity.RESULT_OK) {
            if (listenerControl.have()){
                listenerControl.onPictureError();
            }
            return;
        }

        printLog("onActivityResult-> get picture");
        Bitmap cropBitmap;
        if (data!=null){
            Log.e("Data uri->"+data.getData().toString());
        }

        switch (requestCode){
            case AppRequestCode.CAMERA:
//                Log.d(String.valueOf(tempFileUri));

                if (cameraFileSet!=null){
                    cropBitmap=ImageCrop(
                            AppRequestCode.CAMERA,
                            cameraFileSet.getTempFile().getPath(),
                            getBitmapFromUri(cameraFileSet.getTempFileUri()),
                            cameraFileSet.getScaleX(),
                            cameraFileSet.getScaleY(),
                            true
                    );

                    if (cameraFileSet.getAddDateLabel()){
                        cropBitmap=printDateWaterMarker(cropBitmap,cameraFileSet.getLabelTime());
                    }

                    savePictureTo(cameraFileSet,cropBitmap);
                    cameraFileSet.getTempFile().delete();
                    cameraFileSet=null;
                }
                break;
            case AppRequestCode.PHOTO:
                //取得照片路徑uri

                cropBitmap=ImageCrop(
                        AppRequestCode.PHOTO,
                        data.getData().getPath(),
                        getBitmapFromUri(data.getData()),
                        cameraFileSet.getScaleX(),
                        cameraFileSet.getScaleY(),
                        true
                );
                savePictureTo(cameraFileSet, cropBitmap);
                break;
        }

    }

    private Bitmap printDateWaterMarker(Bitmap src, DateTime time) {
        if (src==null)
            return null;

        int srcW=src.getWidth();
        int srcH=src.getHeight();

        Bitmap waterMarker= BitmapUtil.getTimeWaterMarker(time);
        int markerW=waterMarker.getWidth();
        int markerH=waterMarker.getHeight();
        //create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(srcW,srcH, Bitmap.Config.ARGB_8888);//創建一個新的和src長度寬度一樣的點陣圖
        Canvas cv = new Canvas(newb);
        cv.drawBitmap(src, 0, 0,null);//在0,0座標開始畫入src
        cv.drawBitmap(waterMarker, srcW-markerW,srcH-markerH, null);//在src的右下解畫入浮水印圖片

        cv.save();//保存
        cv.restore();//存儲
        waterMarker.recycle();
        src.recycle();

        return newb;
    }

    //    private void deletFileFromUri(Uri uri){
//
//        Log.e(TAG,"Uri getScheme->"+uri.getScheme());
//        Log.e(TAG,"Uri getAuthority->"+uri.getAuthority());
//
//        Log.e(TAG,"Delet file->"+handleImageOnKitKat(uri));
////        if (file.exists()){
////            file.delete();
////        }
//    }
    @Deprecated
    private String getPathFromUri(Uri uri) {
        String imagePath = null;

        if (DocumentsContract.isDocumentUri(activity, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//                Log.d(TAG, "handleImageOnKitKat 1.1");

                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ((getPackageName()+".fileprovider").equals(uri.getAuthority())) {
//                Log.d(TAG, "handleImageOnKitKat 1.2");

                Uri contentUri = ContentUris.withAppendedId(
                        uri,
                        Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //Log.d(TAG, "content: " + uri.toString());
//            Log.d(TAG, "handleImageOnKitKat 2");
            imagePath = getImagePath(uri, null);
        }
        return imagePath;
    }
    @Deprecated
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = activity.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }
        return path;
    }

    private void savePictureTo(ICameraFileSet set,Bitmap bitmap){
        try {
//            String fileName=sf.format(new Date())+".jpg";
            //建立一個新檔案 設定路徑 名稱
//            File photo=new File(set.getSaveDir().getPath(),set.getFileName());
            //輸出照片，型態為Bitmap
            FileOutputStream fos = new FileOutputStream(set.getSaveFile());
            // 把圖檔壓縮為JPG
            bitmap.compress(Bitmap.CompressFormat.JPEG, ImgCompressQuality, fos);

            fos.flush();
            fos.close();

            Log.i("save picName->"+set.getFileName());
            Log.w("listenerControl have->"+listenerControl.have());


            if (listenerControl.have()){
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    listenerControl.listenerList.forEach((listener)->{
//                        Log.w("activity listener->"+ listener);
//                    });
//                }
                listenerControl.onPicture(set.getSaveFile());
            }
        }catch (Exception e) {
            e.printStackTrace();
            if (listenerControl.have()){
                listenerControl.onPictureError();
            }
        }
    }

//    private void savePictureInApp(Bitmap bitmap){
//        try {
//            String fileName=sf.format(new Date())+".jpg";
//            File photo=new File(appPictureDir.getPath(),fileName);
//
//            //讀取照片，型態為Bitmap
//            FileOutputStream fos = new FileOutputStream(photo);
//            // 把圖檔壓縮為JPG
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//
//            fos.flush();
//            fos.close();
//
//            picName=fileName;
//            Log.i("save picName->"+picName);
//
//            if (listenerControl.have()){
//                listenerControl.onPicture();
//            }
//
//        }catch (Exception e) {
//            e.printStackTrace();
//            picName="";
//            if (listenerControl.have()){
//                listenerControl.onPictureError();
//            }
//        }
//    }

    private Bitmap getBitmapFromUri(Uri uri){

        try {

            ContentResolver cr = activity.getContentResolver();
            return BitmapFactory.decodeStream(cr.openInputStream(uri));
//            return BitmapFactory.decodeFile(uri.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    /**
     * 按照一定的宽高比例裁剪图片
     *
     * @param bitmap
     * @param scaleX
     *            寬的比例
     * @param scaleY
     *            高的比例
     * @return
     */
    private Bitmap ImageCrop(int requestCode,String path,Bitmap bitmap,
                            int scaleX, int scaleY,
                                   boolean isRecycled){
        if (bitmap == null){
            return null;
        }

        int degree=readPictureDegree(path);
        Log.d("raw map angle->"+degree);
        Bitmap newMap=bitmap;
        if (degree>0){
            newMap=rotaingImageView(degree,bitmap);
        }

        int w = newMap.getWidth(); // 得到图片的宽，高
        int h = newMap.getHeight();
        int retX, retY;
        int nw, nh;

        if (w > h){// TODO: 2017/10/16 圖片是橫的

            Log.w("W > H");
            nw = h * scaleX / scaleY;
            nh = h;

            if (nw>w){
                nw=w;
            }
            retX =(w-nw)/2;
            retY =0;

        } else {// TODO: 2017/10/16 圖片是直的

            Log.w("W < H");
            nh = w * scaleY / scaleX;
            nw = w;

            if (nh > h) {
                nh = h;
            }

            retY = (h - nh) / 2;
            retX = 0;

        }
        Log.e(TAG,"Bitmap w->"+w+" Bitmap h->"+h);
        Log.e(TAG,"retX->"+retX+" retY->"+retY+" nW->"+nw+" nH->"+nh);
        Bitmap bmp = Bitmap.createBitmap(newMap, retX, retY, nw, nh, null,
                false);
        if (isRecycled && newMap != null && !newMap.equals(bmp)
                && !newMap.isRecycled()){
            newMap.recycle();
            newMap = null;
        }
        return bmp;// Bitmap.createBitmap(bitmap, retX, retY, nw, nh, null,
        // false);
    }

    /**
     * 讀取照片旋轉角度
     *
     * @param path 照片路徑
     * @return 角度
     */
    private int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /**
     * 旋轉圖片
     * @param angle 被旋轉角度
     * @param bitmap 圖片物件
     * @return 旋轉後的圖片
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {

        Bitmap returnBm = null;
// 根據旋轉角度，生成旋轉矩陣
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
// 將原始圖片按照旋轉矩陣進行旋轉，並得到新的圖片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }


    @Override
    public void onCreat(Activity activity) {

    }

    @Override
    public void onResume(Activity activity) {
        Log.i("---SysCamera-- onResume ->"+activity.getClass().getSimpleName()+"  activity->"+activity);
        formContext(activity);
    }

    @Override
    public void onStop(Activity activity) {

    }

    @Override
    public void onDestory(Activity activity) {

    }

    private class CameraListenerControll{
        private ArrayList<CameraResultListener> listenerList=new ArrayList<>();
        public void addListener(CameraResultListener listener){
            if(!listenerList.contains(listener))
                listenerList.add(listener);
//            listenerList.put(from.getClass().getSimpleName(),listener);
        }
        public void removeListener(CameraResultListener from) {
            if (listenerList.contains(from))
                listenerList.remove(from);
        }
        public boolean have(){
            return listenerList.size()>0;
        }

        public void onPictureError() {
            for (CameraResultListener l:
                 listenerList) {
//                if (l.getKey()==activity.getClass().getSimpleName())
                    l.onPictureError();
            }
        }

        public void onPicture(File pic) {
            for (CameraResultListener l:
                    listenerList) {
//                if (l.getKey()==activity.getClass().getSimpleName())
                    l.onPicture(pic);
            }
        }


    }

}

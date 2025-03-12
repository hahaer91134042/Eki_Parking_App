package com.hill.devlibs.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;


import com.hill.devlibs.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;

/**
 * Created by Hill on 2018/5/16.
 */
public class SharedUtil {

    public static final String LINE_PACKAGE_NAME = "jp.naver.line.android";
    private static final String LINE_SHARE_CLASS_NAME = "jp.naver.line.android.activity.selectchat.SelectChatActivity";


    public static void shareTo(Context context, String subject, String body, String chooserTitle) {
        String shareTex = subject + " " + body;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareTex);
        context.startActivity(Intent.createChooser(sharingIntent, chooserTitle));
    }

    public static void shareTo(Context context, String subject, String body) {
        String shareTex = subject + " " + body;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareTex);
        context.startActivity(sharingIntent);
    }

    public static void shareExludingApp(Context ctx, String subject, String body,@NonNull String... excludeAppNames) {
        if (excludeAppNames==null)
            throw new NullPointerException("Exclude package name can`t be null! You can call shareTo.");

        String shareTex = subject + " " + body;
        List<Intent> targetedShareIntents = new ArrayList<>();

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");

//        Pattern pattern=Pattern.compile("^.*\\b("+excludePkgNames[0]+"|"+excludePkgNames[1]+")\\b.*$");
        Pattern pattern=getRegexPattern(excludeAppNames);

        List<ResolveInfo> resInfo = ctx.getPackageManager().queryIntentActivities(share, 0);
        if (!resInfo.isEmpty()) {

            for (ResolveInfo info : resInfo) {
                String pkgName = info.activityInfo.packageName;
                Matcher matcher=pattern.matcher(pkgName);

//                Log.d("match pkgName->"+pkgName);
//                Log.i("pattern->"+pattern.pattern());
//                Log.w("match->"+ matcher.matches());
//                Log.e("clean code->"+String.valueOf(matcher.replaceAll("")));
                if (!matcher.matches()){
                    Intent targetedShare = createShareIntent(shareTex);
                    int totalNum = targetedShareIntents.size();
                    if (totalNum < 1) {
//                    Log.e("add first->" + pkgName);
                        targetedShare.setPackage(pkgName);
                        targetedShareIntents.add(targetedShare);
                    } else {
                        if (!targetedShareIntents.get(totalNum - 1).getPackage()
                                .equals(pkgName)) {
//                        Log.i("add others->" + pkgName);
                            targetedShare.setPackage(pkgName);
                            targetedShareIntents.add(targetedShare);
                        }
                    }
                }
            }


            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0),
                    ctx.getString(R.string.Share_to));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    targetedShareIntents.toArray(new Parcelable[]{}));
            ctx.startActivity(chooserIntent);
        }

    }

    private static Pattern getRegexPattern(String[] appNames) {
        StringBuilder builder=new StringBuilder("^.*\\b(");
        for (String pkgName:
             appNames) {
            builder.append(pkgName+"|");
        }
        builder.replace(builder.length()-1,builder.length(),"");
        builder.append(")\\b.*$");
        return Pattern.compile(builder.toString());
    }

//    public static void shareExludingApp(Context ctx,String subject,String body, String... excludePkgNames ) {
//        String shareTex=subject+" "+body;
//        List<Intent> targetedShareIntents = new ArrayList<>();
//        Intent share = new Intent(android.content.Intent.ACTION_SEND);
//        share.setType("text/plain");
//
//        List<ResolveInfo> resInfo = ctx.getPackageManager().queryIntentActivities(createShareIntent(shareTex), 0);
//        if (!resInfo.isEmpty()) {
//            for (ResolveInfo fillInfo : resInfo) {
//                Intent targetedShare = createShareIntent(shareTex);
////                if (!fillInfo.activityInfo.packageName.contains(packageNameToExclude)){
////                    targetedShare.setPackage(fillInfo.activityInfo.packageName);
////                    targetedShareIntents.add(targetedShare);
////                }
//
////                if (!fillInfo.activityInfo.packageName.equalsIgnoreCase(packageNameToExclude)) {
////                    targetedShare.setPackage(fillInfo.activityInfo.packageName);
////                    targetedShareIntents.add(targetedShare);
////                }
//                String pkgName=fillInfo.activityInfo.packageName;
//
//
//
//
//                for (String excludePkgName:
//                     excludePkgNames) {
//                    Log.d("Exclude pkgName->"+excludePkgName);
//                    if (pkgName.contains(excludePkgName))
//                        break;
//
//                    int totalNum=targetedShareIntents.size();
//
//                    if (totalNum<1){
//                        Log.e("add first->"+pkgName);
//                        targetedShare.setPackage(pkgName);
//                        targetedShareIntents.add(targetedShare);
//                    }else {
//                        if (!targetedShareIntents.get(totalNum-1).getPackage()
//                                .equals(pkgName)){
//                            Log.i("add others->"+pkgName);
//                            targetedShare.setPackage(pkgName);
//                            targetedShareIntents.add(targetedShare);
//                        }
//                    }
//                }
//
//
////                if (!pkgName.contains(excludePkgName)) {
////                    int totalNum=targetedShareIntents.size();
////
////                    if (totalNum<1){
////                        targetedShare.setPackage(pkgName);
////                        targetedShareIntents.add(targetedShare);
////                    }else {
////                        if (!targetedShareIntents.get(totalNum-1).getPackage()
////                                .equals(pkgName)){
////                            targetedShare.setPackage(pkgName);
////                            targetedShareIntents.add(targetedShare);
////                        }
////                    }
////                }
//
//            }
//
//            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0),
//                    ctx.getString(R.string.Share_to));
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
//                    targetedShareIntents.toArray(new Parcelable[]{}));
//            ctx.startActivity(chooserIntent);
//        }
//
//    }


    public static void shareExludingApp(Context ctx, String packageNameToExclude, Uri imagePath, String text) {
        List<Intent> targetedShareIntents = new ArrayList<Intent>();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        File file = new File(imagePath.getPath());
        List<ResolveInfo> resInfo = ctx.getPackageManager().queryIntentActivities(createShareIntent(text, file), 0);
        if (!resInfo.isEmpty()) {
            for (ResolveInfo info : resInfo) {
                Intent targetedShare = createShareIntent(text, file);

                if (!info.activityInfo.packageName.equalsIgnoreCase(packageNameToExclude)) {
                    targetedShare.setPackage(info.activityInfo.packageName);
                    targetedShareIntents.add(targetedShare);
                }
            }

            Intent chooserIntent = Intent.createChooser(targetedShareIntents.remove(0),
                    "Select app to share");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    targetedShareIntents.toArray(new Parcelable[]{}));
            ctx.startActivity(chooserIntent);
        }

    }

    private static Intent createShareIntent(String text, File file) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        if (text != null) {
            share.putExtra(Intent.EXTRA_TEXT, text);
        }
        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        return share;
    }

    private static Intent createShareIntent(String body) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        if (body != null) {
            share.putExtra(Intent.EXTRA_TEXT, body);
        }
        return share;
    }

}

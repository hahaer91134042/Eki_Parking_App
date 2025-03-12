package com.eki.parking.Controller.frag;

import android.content.SharedPreferences;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import com.hill.devlibs.impl.ISearchable;
import com.hill.devlibs.tools.Log;
import com.hill.devlibs.frag.FragController.FragSwitcher;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;

public abstract class SearchFrag extends BaseFragment<SearchFrag>
                                 implements ISearchable {


    private IFragCallBack fragCallBack;
//    private OnActivitySwitchListener switchListener;
    private ISetToolBarLayout toolBarLayout;

    protected ChildCache childCache=new ChildCache();

    @Deprecated()
    protected SharedPreferences sP;
    @Deprecated()
    protected SharedPreferences.Editor editor;

    /**
     * Cache有以下規則
     * 1.一定要照順序依序存放 不能跳層級
     * 2.從第1層開始
     * 3.同一個層級只存最後顯示的fragment
     */

    protected class ChildCache{
        public LinkedHashMap<Integer,ChildCacheLevel> cacheMap =new LinkedHashMap<>();
        private int nowCacheNum=0;

        public void setCache(ChildCacheLevel childCacheLevel){

            if (childCacheLevel.fragLevel<2){

                cacheMap.put(childCacheLevel.fragLevel-1,childCacheLevel);//強制設定
                Log.e("childCacheLevel.fragLevel<2 add cache List size->"+ cacheMap.size());
                nowCurrentCacheNum();
                for (int i = 0; i < cacheMap.size() ; i++) {
                    Log.d("cache frag->"+ cacheMap.get(i).frag);
                }
            }else {
                nowCurrentCacheNum();
                // TODO:Edit by Hill 2018/8/23 先檢查有沒有第一層
                if (nowCacheNum>0){//至少有第一層
                    // TODO:Edit by Hill 2018/8/23 檢查順序
                    if (nowCacheNum==childCacheLevel.fragLevel-1){
                        cacheMap.put(childCacheLevel.fragLevel-1,childCacheLevel);
                        for (int i = 0; i < cacheMap.size() ; i++) {
                            Log.d("cache frag->"+ cacheMap.get(i).frag);
                        }
                    }else {//沒照順序 EX:只有第一層 直接跳到要記錄第3層
                        throw new NullPointerException("Your cache level do not continuity!!");
                    }
                }else {
                    throw new NullPointerException("You must set level 1 fragment first!!");
                }
            }
        }

        public boolean hasChildCache(){
            return cacheMap.size()>0;
        }

        public ChildCacheLevel getLastCache(){
            nowCurrentCacheNum();
            if (nowCacheNum>0 && cacheMap.get(nowCacheNum-1)!=null){
                return cacheMap.get(nowCacheNum-1);
            }
            return null;
        }

        public ChildFrag getLastCacheFrag(){
            nowCurrentCacheNum();
            if (cacheMap.get(nowCacheNum-1)!=null){
                ChildFrag lastChild= cacheMap.get(nowCacheNum-1).frag;
//                removeLastCache();
                return lastChild;
            }
            return null;
        }

        public ChildFrag getTargetCacheFrag(int fragLevel){
            if (cacheMap.get(nowCacheNum-1)!=null){
                ChildFrag lastChild= cacheMap.get(nowCacheNum-1).frag;
//                cacheMap.removeAt(fragLevel-1);
                return lastChild;
            }
            return null;
        }

        public int nowCurrentCacheNum(){
            nowCacheNum=0;
            for (int i = 0; i < cacheMap.size() ; i++) {
                if (cacheMap.get(i).haveFrag){
                    nowCacheNum++;
                }else {
                    break;
                }
            }
            return nowCacheNum;
        }
        public void removeLastCache() {
            cacheMap.remove(nowCacheNum-1);
            nowCurrentCacheNum();
        }

    }

    protected class ChildCacheLevel {
        public int fragLevel;// TODO:Edit by Hill 2018/8/23 從1層級開始計算
        public ChildFrag frag;
        public boolean haveFrag=false;
        public ChildCacheLevel(@IntRange(from = 1) int level){
            fragLevel=level;
        }

        public ChildCacheLevel setFrag(ChildFrag f){
            frag=null;
            frag=f;
            haveFrag=true;
            return this;
        }
        public void clearFrag(){
            frag=null;
            haveFrag=false;
        }
    }


    public interface IFragCallBack {
        void onSetTitle(String title);
        String onGetTitle();
        void onFragToMain();
        void onBackFrag();
//        void onSetSearchText(String text);
//        void addShopNum();
//        void removeShopNum();
    }

    public interface ISetToolBarLayout{
        View getToolBarActionView();
        void setToolBarActionViewText(@NonNull String text);
    }

//    public interface OnActivitySwitchListener{
//        void onFragToMain();
//        void onBackFrag();
//    }

    public SearchFrag setToolBarLayoutImpl(ISetToolBarLayout l){
        toolBarLayout=l;
        return this;
    }

    protected void setToolBarActionViewText(String text){
        if (toolBarLayout!=null)
            toolBarLayout.setToolBarActionViewText(text);
    }
    protected View getToolBarActionView(){
        if (toolBarLayout!=null)
            return toolBarLayout.getToolBarActionView();
        return null;
    }

    public SearchFrag setFragCallBack(IFragCallBack l){
        fragCallBack =l;
        return this;
    }
//    public SearchFrag setActivitySwitchListener(OnActivitySwitchListener l){
//        switchListener=l;
//        return this;
//    }

    @Override
    public void onSearchStart(@NotNull String text) {

    }

    @Override
    public void onSearch(@NotNull String text) {

    }

    protected void backFrag(){
        if (fragCallBack!=null)
            fragCallBack.onBackFrag();
    }

    protected void toMainActivity(){
        if (fragCallBack!=null)
            fragCallBack.onFragToMain();
    }

    public String getToolBarTitle(){
        if (fragCallBack !=null)
            return fragCallBack.onGetTitle();
        return "";
    }

    public void setToolBarTitle(String title){
        if (fragCallBack !=null)
            fragCallBack.onSetTitle(title);
    }
//    public void setSearchText(String text){
//        if (fragCallBack!=null)
//            fragCallBack.onSetSearchText(text);
//    }



    @Override
    public void replaceFragment(SearchFrag fragment, String targetTag, String backTag,FragSwitcher switchType) {
        if (fragCallBack !=null)
            fragment.setFragCallBack(fragCallBack);

        super.replaceFragment(fragment, targetTag, backTag, switchType);
    }

    protected void replaceFragWithCacheChildFrag(ChildCacheLevel cacheLevel , FragSwitcher switchType){
        replaceFragWithCacheChildFrag(cacheLevel,switchType,childFragLoaderRes);
    }

    protected void replaceFragWithCacheChildFrag(ChildCacheLevel cacheLevel , FragSwitcher switchType,int loaderRes){
        if (cacheLevel.fragLevel<2){
            replaceFragment(cacheLevel.frag,cacheLevel.frag.TAG,null,switchType);
        }else {
            Log.e("Start save cache level->"+cacheLevel.fragLevel);
            childCache.setCache(
                    new ChildCacheLevel(cacheLevel.fragLevel-1)
                            .setFrag(getScreenChildFrag(loaderRes))
            );

//            replaceFragment(cacheLevel.frag,cacheLevel.frag.TAG,getScreenFragTag(childFragLoaderRes),switchType);
            replaceFragment(cacheLevel.frag,cacheLevel.frag.TAG,null,switchType);//用自己的 cache
        }
    }

    public boolean fragBackToMain(){
        return true;
    }

    protected void switchLastChildFrag(){
        ChildCacheLevel lastCache=childCache.getLastCache();
        if (lastCache!=null){
            replaceFragment(lastCache.frag,lastCache.frag.TAG,null,FragSwitcher.LEFT_IN);
            childCache.removeLastCache();
        }
    }
    public boolean hasChildCache(){
        return childCache.hasChildCache();
    }

}

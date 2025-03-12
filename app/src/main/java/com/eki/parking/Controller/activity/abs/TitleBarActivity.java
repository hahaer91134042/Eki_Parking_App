package com.eki.parking.Controller.activity.abs;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cursoradapter.widget.CursorAdapter;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.eki.parking.R;
import com.hill.devlibs.frag.FragController.FragSwitcher;
import com.eki.parking.Controller.frag.SearchFrag;

import com.eki.parking.Controller.util.ScreenUtils;
import com.hill.devlibs.impl.IActivityFeatureSet;
import com.hill.devlibs.impl.ISearchable;
import com.hill.devlibs.tools.Log;
import com.hill.devlibs.util.StringUtil;

import java.util.ArrayList;


/**
 * Created by Hill on 2017/11/20.
 */

public abstract class TitleBarActivity extends BaseActivity<SearchFrag>
                                       implements SearchFrag.IFragCallBack{
//                                                  SearchFrag.OnActivitySwitchListener{

    public interface ToolbarIconSet{
        @DrawableRes int toolbarIcon();
    }
    public interface SetToolbar{
        void setUpToolbar(@NonNull Toolbar toolbar);
    }

    protected Toolbar mToolbar;
    protected ActionBar actionBar;
    private MenuItem toolBarMenuItem;
    private boolean isShowToolActionView=true;
    protected IActivityBackEvent backEvent;

//    private ShoppingCarCountIcon carCountView;
//    private LineActionIcon lineActionBtn;

    private IActivityFeatureSet featureSet=setActivityFeature();

    protected abstract IActivityFeatureSet setActivityFeature();

//    private ActivityClassEnum classEnum=getTitleBarClass();
//    protected SearchView searchView;

//    private FrameLayout redCircleView;
//    private TextView shopNumText;
//    private int shopNumCount=0;
//    private ActivityClassEnum classEnum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getOnBackPressedDispatcher().addCallback(this,backPress);
        initTitleBar();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        // TODO:Edit by Hill 2018/8/21 可能要改 

    }

    @Override
    protected void onPause() {
        // TODO:Edit by Hill 2018/8/21 可能要改

        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    //藥用自訂的可以傳0
    protected int toolBarRes(){
        return R.id.toolbar;
    }

    private void initTitleBar() {

        if(toolBarRes()>0){
            mToolbar = findViewById(toolBarRes());
//        mToolbar.setBackgroundColor(getResources().getColor(R.color.color_orange));

            if (this instanceof SetToolbar)
                ((SetToolbar) this).setUpToolbar(mToolbar);

            setSupportActionBar(mToolbar);

            actionBar = getSupportActionBar();

            if (this instanceof ToolbarIconSet)
                actionBar.setHomeAsUpIndicator(((ToolbarIconSet) this).toolbarIcon());

//            if (toolBarBackBtn()>0)
//                actionBar.setHomeAsUpIndicator(toolBarBackBtn());

            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);

                if (!isMain()) {
                    showToolBarBackHomeBtn();
                }
            }
        }
    }


    // TODO:Edit by Hill 2018/5/22 以後可能會需要修改
    private void onToolBarBackPress() {
        SearchFrag onScreenFrag = getScreenFrag();
        Log.w("OnScreen Frag->"+onScreenFrag);

        if (onScreenFrag != null) {
            boolean hasFragChild=onScreenFrag.hasChildCache();//必要 不然執行onBackPress會變動

            onScreenFrag.onBackPress();

            int cacheFragNum=fragLevelCache.nowCurrentCacheNum();

            Log.i("onBack frag cache num->"+cacheFragNum);

            if (cacheFragNum>0){

                if (!hasFragChild){//有 child cache先讓各自的child 退回 先不要退回parent
                    replaceFragment(
                            new FragLevelSet(cacheFragNum).setFrag(fragLevelCache.getCacheFrag(cacheFragNum)),
                            FragSwitcher.LEFT_IN);

                    if (cacheFragNum==1){
                        whenBackToFragLv1();
                        if (isMain()){
                            hideToolBarBackHomeBtn();
                        }else { // 右上角菜單
                            openToolBarActionView();
                        }
                    }
                    fragLevelCache.removeLastCache();
                    Log.w("Removed last frag cache num->"+fragLevelCache.nowCacheNum);
                }
            }else {
                if (!isMain()){
                    if (onScreenFrag.fragBackToMain())
                        toMain();
                }
            }
        }else {
            if (!isMain())
                toMain();
        }
    }


    public void closeToolBarActionView() {
        isShowToolActionView=false;
        if (toolBarMenuItem!=null)
            toolBarMenuItem.setVisible(false);
    }

    public void openToolBarActionView() {
        isShowToolActionView=true;
        if (toolBarMenuItem!=null)
            toolBarMenuItem.setVisible(true);
    }

    protected void whenBackToFragLv1(){
    }
//    private OnBackPressedCallback backPress=new OnBackPressedCallback(true) {
//        @Override
//        public void handleOnBackPressed() {
//            Log.i("onBackpress->"+classTag);
//        }
//    };


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
//        Log.w("KeyEvent->"+event.getKeyCode()+"  backEvent->"+KeyEvent.KEYCODE_BACK);
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onBackPressed() {
        //這個不用去執行 因為本專案有自己退回fragment的方式
//        super.onBackPressed();
        if (backEvent!=null){
            if (backEvent.onBackPress())
                onToolBarBackPress();
        }else {
            if (this instanceof IActivityBackEvent){
                if (((IActivityBackEvent)this).onBackPress())
                    onToolBarBackPress();
            } else {
                onToolBarBackPress();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_about:
//                // About option clicked.
//                HHLog.d("--menu about--");
////                mToolbar.setTitle("test title");
////                addShopNum();
//                return true;
//            case R.id.action_exit:
//                // Exit option clicked.
//                HHLog.i("--menu exit--");
////                removeShopNum();
////                closeToolBarActionView();
//                return true;
//            case R.id.action_settings:
//                // Settings option clicked.
//                HHLog.w("--menu setting--");
//                openToolBarActionView();
//                return true;
            case android.R.id.home://返回鍵
                Log.e(classTag+" --back to home--");
//                onToolBarBackPress();
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(featureSet.getMenuRes(), menu);

        if(this instanceof IToolBarItemSet){
            IToolBarItemSet impl=(IToolBarItemSet)this;
            toolBarMenuItem=menu.findItem(impl.itemRes());
            impl.initMenuItem(toolBarMenuItem);
        }

        if (this instanceof IToolBarItemSetList){
            IToolBarItemSetList impl=(IToolBarItemSetList)this;
            for (IToolBarItemSet set : impl.itemSetList()) {
                MenuItem item=menu.findItem(set.itemRes());
                set.initMenuItem(item);
            }
        }

        //這邊DrawerLayout開關drawer會觸發  所以要檢查現在的開關狀態
        if (isShowToolActionView)
            openToolBarActionView();
        else
            closeToolBarActionView();

//        switch (classEnum) {
//            case Main:
////            case ProductList:
//
//
//                //以後用IMenuItem
////                toolBarMenuItem = menu.findItem(R.id.my_search);
//
////                searchView = initSearchView(toolBarMenuItem);
////                LinearLayout parent= toolBarMenuItem.getActionView().findViewById(R.id.parentView);
////                LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
////                        getWidth()*8/10,
////                        ViewGroup.LayoutParams.MATCH_PARENT
////                );
////                parent.setLayoutParams(lp);
////
////                searchView=parent.findViewById(R.id.searchText);
////                setUpSearchText(searchView);
//
////                initShopCarCounter(menu);
////                initLineActionBtn(menu);
//                break;
//            case Login://需要再用
//
//
//                break;
//            case DateSearch://需要再用
//
//                break;
//            case Search://目前沒用
////                toolBarMenuItem = menu.findItem(R.id.my_search);
////
////                searchView = initSearchView(toolBarMenuItem);
////                setUpSearchText(searchView);
//                break;
//        }

        return true;
    }

//    protected abstract ActivityClassEnum getTitleBarClass();

//    private void initShopCarCounter(Menu menu) {
//        MenuItem item = menu.findItem(R.id.shoppingCarCounter);
//        carCountView = item.getActionView().findViewById(R.id.shoppingCarIcon);
//    }
//
//    private void initLineActionBtn(Menu menu) {
//        MenuItem item = menu.findItem(R.id.lineActionBtn);
//        lineActionBtn = item.getActionView().findViewById(R.id.lineActionIcon);
//        lineActionBtn.setPadding(0, 0, ScreenUtils.dpToPx(5), 0);
//        lineActionBtn.setOnClickListener(v -> {
//            showToast("此功能目前未開放");
//        });
//    }

    private void setUpSearchText(TextView searchView) {
//        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        int padding = ScreenUtils.dpToPx(2);


        searchView.setPadding(padding, padding, padding, padding);
        searchView.setBackgroundResource(R.drawable.stroke_round_corner_orange);
//        searchView.setCompoundDrawables(getDrawable(R.this.icon_search_orange),null,null,null);

        searchView.setGravity(Gravity.CENTER_VERTICAL);
        searchView.setTextColor(Color.BLACK);
        searchView.setTextSize(ScreenUtils.dpToPx(5));
        searchView.setHintTextColor(Color.GRAY);
        searchView.setOnClickListener(v -> {
//            HHLog.e("go to search");
            //startActivitySwitchAnim(SearchActivity.class);
        });
        StringUtil.getImgStringBuilder()
                .setIcon(R.drawable.icon_search_orange)
                .setHint(getString(R.string.Search_Address))
                .into(searchView);
    }

    @Deprecated
    private void setUpSearchText(SearchView searchView) {
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        int padding = ScreenUtils.dpToPx(2);
        searchEditText.setPadding(padding, padding, padding, padding);
        searchEditText.setBackgroundResource(R.drawable.stroke_round_corner_orange);


        searchEditText.setGravity(Gravity.CENTER_VERTICAL);
        searchEditText.setTextColor(Color.BLACK);
        searchEditText.setTextSize(ScreenUtils.dpToPx(5));
        searchEditText.setHintTextColor(Color.GRAY);

        StringUtil.getImgStringBuilder()
                .setIcon(R.drawable.icon_search_orange)
                .setHint(getString(R.string.Search_Address))
                .into(searchEditText);
    }

    @Deprecated
    private SearchView initSearchView(MenuItem item) {

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();


        searchView.setMaxWidth(getWidth() * 4 / 5);//set search view expansion Max width
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Log.w("onQueryTextSubmit->" + query);
                startQuery(query,true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                Log.i("onQueryTextChange->" + newText);
//                Log.d("get Screen Frag->"+getScreenFrag());
//                getScreenFrag().onSearch(newText);
                startQuery(newText,false);
                return true;
            }
        });

        // 這邊讓icon可以還原到搜尋的icon
        searchView.setIconifiedByDefault(true);
//        switch (classEnum) {
//            case Main:
////            case ProductList:
//            case Search:
////                if (classEnum.equals(ActivityClassEnum.Search))
//                searchView.onActionViewExpanded();
//                searchView.clearFocus();
//                //以後要開搜尋的時候要開
////                SearchSuggestionsAdapter adapter = new SearchSuggestionsAdapter(this, null);
////                searchView.setSuggestionsAdapter(adapter);
//                searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
//                    @Override
//                    public boolean onSuggestionSelect(int position) {
//                        Cursor c = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
//                        searchView.setQuery(c.getString(1), true);
//                        searchView.clearFocus();
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onSuggestionClick(int position) {
//                        Cursor c = (Cursor) searchView.getSuggestionsAdapter().getItem(position);
////                        HHLog.e("onSuggestionClick position->"+position);
//                        searchView.setQuery(c.getString(1), true);
//                        searchView.clearFocus();
//                        return true;
//                    }
//                });
//                break;
//        }

        return searchView;
    }

    protected void startQuery(String queryTex,boolean isStart) {
        // TODO:Edit by Hill 2018/7/19 先去除特殊字元
        queryTex = StringUtil.removeEscapeChars(queryTex);
        queryTex = StringUtil.removeSpecialChar(queryTex);

//        Log.w("---StartQuery search->" + queryTex);
        SearchFrag searchFrag = getScreenFrag();
        if (searchFrag != null)
            if (searchFrag instanceof ISearchable) {
                if (isStart)
                    searchFrag.onSearchStart(queryTex);
                else
                    searchFrag.onSearch(queryTex);
            }
//        Log.i("---StartQuery SearchFrag->"+searchFrag);



        // TODO:Edit by Hill 2018/7/19 記錄使用者搜尋過的字串
//        switch (classEnum) {
//            case ProductList:
//            case Search:
//                if (!queryTex.isEmpty()){
//                    SearchListVO vo = new SearchListVO();
//                    vo.setSearchKey(queryTex);
//                    vo.setTime(System.currentTimeMillis());
//                    getApp().getSqlManager().saveSearchVO(vo);
//                }
//                break;
//        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(getScreenFrag()!=null)
//            getScreenFrag().onActivityResult(requestCode, resultCode, data);
//    }

    //    private Cursor getSearchCursor() {
//        String[] columns = DbColumn.SEARCH_LIST.TABLE;
//        Uri uri = DBContentProvider.getCONTENT_URI_SearchList();
//        DbUtil.QueryArgs queryBuilder = DbUtil.getQueryArgs();
//        queryBuilder.setOrderBy(columns[0], true);
//        queryBuilder.setLimit(5);
//
//        return getApp().getSqlManager().sqlQuery(uri, columns, queryBuilder);
//    }
//    public static class SearchSuggestionsAdapter extends CursorAdapter{
//
////        public SearchSuggestionsAdapter(Context context){
////            this(context,,false);
////
////        }
//        private LayoutInflater inflater;
//
//        public SearchSuggestionsAdapter(Context context, Cursor c, boolean autoRequery) {
//            super(context, c, autoRequery);
//            inflater=LayoutInflater.from(context);
//        }
//
//        @Override
//        public View newView(Context context, Cursor cursor, ViewGroup parent) {
//            return inflater.inflate(R.layout.item_textview,parent,false);
//        }
//
//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            TextView textView=view.findViewById(R.id.textView);
//            textView.setText(cursor.getString(cursor.getColumnIndex(DbColumn.SEARCH_LIST.TABLE[1])));
//        }
//    }

    public static class SearchSuggestionsAdapter extends CursorAdapter {
        private static final String[] mFields = {"_id", "result"};
//        private static final String[] mVisible = {"result"};
//        private static final int[] mViewIds = {R.id.textView};
//        private ArrayList<SearchListVO> searchList;
//        private SuggestionsCursor mCursor;

        private LayoutInflater inflater;

        public SearchSuggestionsAdapter(Context context, ArrayList<String> list) {
            super(context, null, false);
            //searchList = List;
            inflater = LayoutInflater.from(context);

        }


//        public SearchSuggestionsAdapter(Context context) {
//            super(context, R.layout.item_textview, null, mVisible, mViewIds, 0);
//        }

//        @Override
//        public void bindView(View view, Context context, Cursor cursor) {
//            super.bindView(view, context, cursor);
//        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = inflater.inflate(R.layout.item_textview, parent, false);
            view.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
//            view.getLayoutParams().height=ScreenUtils.dpToPx(12);
            view.setBackgroundColor(Color.WHITE);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textView = view.findViewById(R.id.textView);
            textView.setText(cursor.getString(cursor.getColumnIndex(mFields[1])));
            textView.setTextSize(ScreenUtils.dpToPx(8));
//            textView.setGravity(Gravity.CENTER_VERTICAL);
        }

//        public String getSearchKey(int position) {
//            return searchList.get(position).getSearchKey();
//        }

//        @Override
//        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
//            return new SuggestionsCursor(constraint, searchList);
//        }

//        private static class SuggestionsCursor extends AbstractCursor {
//            private ArrayList<String> mResults = new ArrayList<>();
//
//            //            private ArrayList<String> queryList = new ArrayList<>();
//            public SuggestionsCursor(CharSequence constraint, ArrayList<SearchListVO> List) {
//                mResults.clear();//必要 因為是static class
//                final int count = List.size();
//                for (int i = 0; i < count; i++) {
//                    mResults.add(List.get(i).getSearchKey());
//                }
////                HHLog.e("SuggestionsCursor constraint->"+constraint);
//                if (!TextUtils.isEmpty(constraint)) {
//                    String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
////                    HHLog.d("constrainString->"+constraintString);
//                    Iterator<String> iter = mResults.iterator();
//                    while (iter.hasNext()) {
//                        String next = iter.next();
////                        HHLog.i("next ratio->"+next);
////                        HHLog.w("if (!iter.next().toLowerCase(Locale.ROOT).startsWith(constraintString))->"+!next.toLowerCase(Locale.ROOT).startsWith(constraintString));
//                        if (!next.toLowerCase(Locale.ROOT).startsWith(constraintString)) {
//                            iter.remove();
//                        }
//                    }
//                }
//            }
//
//
//            @Override
//            public int getCount() {
//                return mResults.size();
//            }
//
//            @Override
//            public String[] getColumnNames() {
//                return mFields;
//            }
//
//            @Override
//            public long getLong(int column) {
//                if (column == 0) {
//                    return mPos;
//                }
//                throw new UnsupportedOperationException("unimplemented");
//            }
//
//            @Override
//            public String getString(int column) {
//                if (column == 1) {
//                    return mResults.get(mPos);
//                }
//                throw new UnsupportedOperationException("unimplemented");
//            }
//
//            @Override
//            public short getShort(int column) {
//                throw new UnsupportedOperationException("unimplemented");
//            }
//
//            @Override
//            public int getInt(int column) {
//                throw new UnsupportedOperationException("unimplemented");
//            }
//
//            @Override
//            public float getFloat(int column) {
//                throw new UnsupportedOperationException("unimplemented");
//            }
//
//            @Override
//            public double getDouble(int column) {
//                throw new UnsupportedOperationException("unimplemented");
//            }
//
//            @Override
//            public boolean isNull(int column) {
//                return false;
//            }
//        }
    }

    //---------searchView interface------------
    @Override
    public void onSetTitle(String title) {
        if (mToolbar != null)
            mToolbar.setTitle(title);
    }

    @Override
    public String onGetTitle() {
        if (mToolbar != null)
            return mToolbar.getTitle().toString();
        return "";
    }

    @Override
    public void onFragToMain() {
        toMain();
    }

    @Override
    public void onBackFrag() {
        onBackPressed();
    }


    //    @Override
//    public void onSetSearchText(String text) {
//        if (searchView != null)
//            searchView.setText(text);
//    }

    protected void showToolBarBackHomeBtn() {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    protected void hideToolBarBackHomeBtn() {
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }

    //------------------------------------------



    @Override
    public void replaceFragment(SearchFrag fragment, String targetTag, String backTag, FragSwitcher switchType) {
//        Log.w("TitleBarActivity replaceFragment tag->"+targetTag+" back->"+backTag);
        fragment.setFragCallBack(this);
//        fragment.setActivitySwitchListener(this);
        if(this instanceof SearchFrag.ISetToolBarLayout)
            fragment.setToolBarLayoutImpl((SearchFrag.ISetToolBarLayout) this);

        if (!StringUtil.isEmptyString(backTag)) {//有第2層的frag
            showToolBarBackHomeBtn();
//            SearchFrag onScreenFrag = getScreenFrag();
//            if (onScreenFrag != null)
//                fragListMap.put(backTag, onScreenFrag);
        }

        super.replaceFragment(fragment, targetTag, backTag, switchType);
    }


    final protected void switchToNextFrag(SearchFrag nextFrag, @IntRange(from = 2) int level){
        replaceFragment(new FragLevelSet(level).setFrag(nextFrag),FragSwitcher.RIGHT_IN);
    }


}

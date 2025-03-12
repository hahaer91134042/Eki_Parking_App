package com.eki.parking.View.pager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.eki.parking.Controller.frag.BaseFragment;
import com.hill.devlibs.tools.Log;

import java.util.List;

/**
 * Created by Hill on 2017/7/19.
 */

public class ViewPagerFragAdapter <F extends BaseFragment>extends FragmentStatePagerAdapter {

    private List<F> fragmentList;
    private List<String> titleList;
    private FragmentManager fragManager;
//    private SparseArray<Fragment> onLoadFragList=new SparseArray<>();


    public ViewPagerFragAdapter(FragmentManager fm,List<F> list){
        this(fm,list,null);
    }
    public ViewPagerFragAdapter(FragmentManager fm,List<F> list,
                                List<String> titleList) {
        super(fm);
        fragManager=fm;
        this.fragmentList = list;
        this.titleList = titleList;
    }


    //    private String makeFragmentName(int layoutRes, int index) {
//        return "android:switcher:" + layoutRes + ":" + index;
//    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        Fragment f=fragManager.findFragmentById(container.getId());
        Fragment f=(Fragment)super.instantiateItem(container,position);
        Log.w("view pager frag->"+f.getClass().getSimpleName());
//        PrintLogKt.e("---instantiateItem--","\nfrag in List->"+fragmentList.get(position)
//                +"\nfrag in view->"+f
//                +"\nLoad frag position->"+position);
//        onLoadFragList.put(position,f);
//        if (!fragmentList.get(position).equals(f))
//            return fragmentList.get(position);
//
////       tagList.put(position, makeFragmentName(container.getId(),position));
        return f;
//        return fragmentList.get(position);
    }



    // TODO: 2017/11/29 http://www.jianshu.com/p/266861496508
//    @Override
//    public int getItemPosition(Object object) {
////        PrintLogKt.w("---getItemPosition->"+object);
//        return POSITION_NONE;
//    }
////    @Override
//    public int getItemPosition(Object object) {
//        PrintLogKt.w("---getItemPosition->"+object);
//
//        int arg=POSITION_UNCHANGED;
//
//        if (onLoadFragList.indexOfValue((Fragment) object)<0)
//            arg=POSITION_NONE;
//
//        PrintLogKt.i("item change->"+arg);
//
//        return arg;
//    }

//    @Override
//    public void startUpdate(ViewGroup container) {
//        PrintLogKt.i("---startUpdate--","\nview id->"+container.getId());
//        super.startUpdate(container);
//    }
//
//    @Override
//    public void finishUpdate(ViewGroup container) {
//        PrintLogKt.w("---finishUpdate--","\nview id->"+container.getId());
//        super.finishUpdate(container);
//    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        PrintLogKt.e("---destroyItem--","\nview id->"+container.getId()+"\nposition->"+position+"\nobj->"+object);
//        onLoadFragList.remove(position);
        super.destroyItem(container, position, object);
    }

    // ViewPage中显示的内容
    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
//        PrintLogKt.i("---getItem---> item position "+position+"\n frag->"+fragmentList.get(position));
        return (fragmentList == null || fragmentList.size() == 0) ? null
                : fragmentList.get(position);
    }

    // Title中显示的内容
    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
//        return (titleList!=null ||(titleList.size() > position)) ? titleList.get(position) : "";
        if (titleList!=null && titleList.size()>0)
            return titleList.get(position);
        return "";
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fragmentList == null ? 0 : fragmentList.size();
    }

    public void refresh(List<F> list) {
//        fragmentList=new ArrayList<>(List);
        fragmentList=list;

        notifyDataSetChanged();
    }

//    private void compareAndRefreshList(List<Object> refreshList) {
//        final CompareViewItem diffCallback = new CompareViewItem(itemList, refreshList);
//        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback, true);
//
//
//        itemList = refreshList;
//        diffResult.dispatchUpdatesTo(this);
////        notifyDataSetChanged();//將顯示資料變更成新的
//    }
//
//    private class CompareViewItem extends DiffUtil.Callback{
//
//        @Override
//        public int getOldListSize() {
//            return 0;
//        }
//
//        @Override
//        public int getNewListSize() {
//            return 0;
//        }
//
//        @Override
//        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//            return false;
//        }
//
//        @Override
//        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//            return false;
//        }
//    }
}
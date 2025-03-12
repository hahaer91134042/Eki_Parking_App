package com.eki.parking.Controller.activity.frag.Main.leftMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.eki.parking.AppFlag;
import com.eki.parking.Controller.activity.BillingOverviewActivity;
import com.eki.parking.Controller.activity.CarSettingActivity;
import com.eki.parking.Controller.activity.ConsumptionHistoryActivity;
import com.eki.parking.Controller.activity.LandRuleActivity;
import com.eki.parking.Controller.activity.LoginActivity;
import com.eki.parking.Controller.activity.ParkingRuleActivity;
import com.eki.parking.Controller.activity.QuestionActivity;
import com.eki.parking.Controller.activity.SiteOpenActivity;
import com.eki.parking.Controller.activity.SiteReservaStatusActivity;
import com.eki.parking.Controller.activity.intent.SiteListIntent;
import com.eki.parking.Controller.builder.BeManagerSerialBuilder;
import com.eki.parking.Controller.dialog.MsgDialog;
import com.eki.parking.Controller.dialog.SimulatePageDialog;
import com.eki.parking.Controller.frag.BaseFragment;
import com.eki.parking.Controller.listener.OnMenuSelectListener;
import com.eki.parking.Controller.listener.OptionListener;
import com.eki.parking.Model.EnumClass.IdentityMode;
import com.eki.parking.Model.EnumClass.MenuOptionEnum;
import com.eki.parking.Model.sql.EkiMember;
import com.eki.parking.R;
import com.eki.parking.View.widget.LeftDrawerOptionPanel;
import com.eki.parking.View.widget.UserModeIcon;
import com.eki.parking.View.widget.UserOptionBtn;
import com.hill.devlibs.impl.IFragViewRes;
import com.hill.devlibs.listener.OnMsgDialogBtnListener;
import com.hill.devlibs.tools.Log;

/**
 * Created by Hill on 2019/4/29
 */
//------------側邊攔裡面的view----------------------------
public class NavigationDrawerFrag extends BaseFragment<NavigationDrawerFrag>
                                  implements OnMenuSelectListener, IFragViewRes {
    public interface DrawerSwitchCallBack{
        void onOpenDrawer(boolean isAnim);
        void onCloseDrawer(boolean isAnim);
    }

//    StateButton mLoginBtn;
    private LeftDrawerOptionPanel mLeftDrawerPanel;
    private DrawerSwitchCallBack drawerCB;
//    private SysCameraManager cameraManager;
    private View userModeView;
    private UserOptionBtn optionBtn;
    private OptionListController optionListController;
//    private UserIconCameraView userIcon;
    private TextView userNameText;
    private UserModeIcon userModeIcon;

    @Override
    public int setFragViewRes() {
        return R.layout.frag_navigation_drawer;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void setDrawerCallkBack(DrawerSwitchCallBack cb){
        drawerCB=cb;
    }
    @Override
    protected void initFragView() {
        View view=getView();
        mLeftDrawerPanel=view.findViewById(R.id.leftDrawerPanel);
        userModeView=view.findViewById(R.id.userModeView);
        optionBtn=view.findViewById(R.id.userOptionBtn);
        userModeIcon=view.findViewById(R.id.userModeIcon);
        userNameText=view.findViewById(R.id.userNameText);
        mLeftDrawerPanel.setMenuListener(this);

        optionListController=new OptionListController();

        registerReceiver(AppFlag.OnMemberCheck,AppFlag.OnLogout);
        userModeView.setOnClickListener(v->{
            //避免穿透用
        });
    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.w("----Left drawer onResume----");
    }

    @Override
    public void onStop() {
        super.onStop();
//        Log.i("----Left drawer onStop----");
    }

    @Override
    protected void onCatchReceive(String action, Intent intent) {
        Log.w("Left drawer catch receiver ->"+action);
        switch (action){
            case AppFlag.OnLogout:
            case AppFlag.OnMemberCheck:

                optionListController.refreshLeftDrawer();

                break;
        }
    }

    @Override
    public void onDestroyView() {
//        Log.v(TAG+"->onDestroyView()");
//        cameraManager.removeCameraListener(getContext(),this);
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("drawerFrag onAvtivityResult");
//        cameraManager.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onMenuSelect(MenuOptionEnum menuOption) {
        Log.d("Click option->"+menuOption);

        switch (menuOption){
            case MemberProblemResponse:
                Intent i1=new Intent();
                i1.setClass(getContext(),QuestionActivity.class);
                i1.putExtra(AppFlag.DATA_FLAG,QuestionActivity.MemberQuestion);
                startActivitySwitchAnim(i1,false);
                break;
            case ManagerProblemResponse:
                Intent i2=new Intent();
                i2.setClass(getContext(),QuestionActivity.class);
                i2.putExtra(AppFlag.DATA_FLAG,QuestionActivity.ManagerQuestion);
                startActivitySwitchAnim(i2,false);
                break;

            case ProblemResponse:
                Intent i3=new Intent();
                i3.setClass(getContext(),QuestionActivity.class);
                i3.putExtra(AppFlag.DATA_FLAG,QuestionActivity.NoneLogin);
                startActivitySwitchAnim(i3,false);
                break;
            case ParkingSiteSetting://車位設定
                startActivitySwitchAnim(new SiteListIntent(getActivity()),false);
                break;
            case ReservationStatus:
                startActivitySwitchAnim(SiteReservaStatusActivity.class,false);
                break;
            case SiteOpenTime:
                startActivitySwitchAnim(SiteOpenActivity.class,false);
                break;

            case BillingOverView://帳單總覽
                startActivitySwitchAnim(BillingOverviewActivity.class,false);
                break;


            case CarSetting://愛車設定
                startActivitySwitchAnim(CarSettingActivity.class,false);
                break;
//            case Calendar://行事曆
//                startActivitySwitchAnim(ParkingCalendarActivity.class,false);
//                break;
            case ConsumptionHistory://消費歷程
                startActivitySwitchAnim(ConsumptionHistoryActivity.class,false);
                break;

            case LandOwnerRule:
                startActivitySwitchAnim(LandRuleActivity.class,false);
                break;
            case ParkingRules:
                startActivitySwitchAnim(ParkingRuleActivity.class,false);

                break;
            case LoginOrRegister:
                startActivitySwitchAnim(LoginActivity.class);
                break;
            case Logout:
                //有點卡卡
                if (drawerCB!=null)
                    drawerCB.onCloseDrawer(true);

                new MsgDialog(getContext()).setShowMsg(getString(R.string.Are_you_sure_you_want_to_log_out))
                        .setNegativeBtn(getString(R.string.Determine))
                        .setPositiveBtn(getString(R.string.Cancel))
                        .setBtnClickListener(new OnMsgDialogBtnListener() {
                            @Override
                            public void onPostiveBtn() {

                            }

                            @Override
                            public void onNegativeBtn() {
                                getApp().startLogout();

                            }
                        }).show();
                break;
        }

    }

    //----------------------------------------

    private class OptionListController implements OptionListener<IdentityMode>{

        private boolean isLogin=false;

        public OptionListController() {
            optionBtn.setListener(this);

            refreshLeftDrawer();
        }

        public void refreshLeftDrawer() {

            isLogin=getSqlManager().hasData(EkiMember.class);

            if(isLogin){
                EkiMember member=getSqlManager().getObjData(EkiMember.class);
                optionBtn.setVisibility(View.VISIBLE);
                optionBtn.setMember(member);

                userModeIcon.setVisibility(View.VISIBLE);
                userModeView.setVisibility(View.VISIBLE);
                userModeView.setBackgroundResource(R.drawable.shap_drawer_user_mode_bg);
                userModeIcon.useCarMaster();
                userNameText.setText(member.getInfo().getNickName());
                mLeftDrawerPanel.memberOption();

            }else {
                optionBtn.setVisibility(View.INVISIBLE);
                userModeView.setBackgroundResource(R.drawable.shap_drawer_guest_mode_bg);
                userModeIcon.useGuest();
                userNameText.setText(getString(R.string.Guest));
                mLeftDrawerPanel.anonymousOption();
            }
        }

        @Override
        public void onOptionClick(IdentityMode mode) {
            switch (mode){
                case User:
                    userModeView.setBackgroundResource(R.drawable.shap_drawer_user_mode_bg);
                    userModeIcon.useCarMaster();
                    mLeftDrawerPanel.memberOption();
                    break;
                case LandOwner:
                    userModeView.setBackgroundResource(R.drawable.shap_drawer_land_owner_mode_bg);
                    userModeIcon.useHouseMaster();
                    mLeftDrawerPanel.managerOption();
                    break;
                case BeManager:
                    Log.i("be come manager");
                    BeManagerSerialBuilder builder=new BeManagerSerialBuilder();

                    builder.setFinishBack(()->{
                        refreshLeftDrawer();
                    });

                    SimulatePageDialog dialog=new SimulatePageDialog(builder);
                    dialog.show(getChildFragmentManager(),dialog.TAG);

                    break;
            }
        }
    }
}

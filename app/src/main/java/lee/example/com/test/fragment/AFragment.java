package lee.example.com.test.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import lee.example.com.test.R;

public class AFragment extends Fragment {
        private TextView mTvTitle;
        private Button mBtnChange,mBtnReset,mBtnMessage;
        private BFragment bFragment;
        private IonMessageClick listener;

// fragemt和Activity发生关系是会调用onAttach这个方法 所以可以用这个进行传递
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            //将方法里面的context强制转型到IonMessageClick接口；
            listener = (IonMessageClick)context;
        }catch ( ClassCastException e ){
            throw new ClassCastException("强制转化失败 Activity必须要实现IonMessageClick接口 ");
        }
    }

    //设置布局文件！
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //相当于Activity中的setContentView(); 他的返回值为view 所以直接返回view就行
        View view =inflater.inflate(R.layout.fragment_a,container,false);
        //在BFragment返回之后重新创建了这个方法！
        Log.d("AFragment", "-------------onCreateView--------------- ");
        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvTitle = view.findViewById(R.id.tv_titlefragment);
        mBtnChange = view.findViewById(R.id.btn_change);
        mBtnReset = view.findViewById(R.id.btn_reset);
        mBtnMessage = view.findViewById(R.id.btn_message);

        mBtnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bFragment == null){
                    bFragment = new BFragment();
                }
                Fragment fragment = getFragmentManager().findFragmentByTag("a");
                if (fragment != null){
                    //这个目的是判断是否有aFragment的tag 如果没有将fragment隐藏 然后在add bFragment
                    getFragmentManager().beginTransaction().hide(fragment).add(R.id.fl_container,bFragment).addToBackStack(null).commitAllowingStateLoss();
                }else {
                    //在commitAllowingStateLoss之前添加addToBackStack添加到回退栈里面去 在BFragment按返回的时候就只会回退到前一页 不会回退到主ACT中
                    getFragmentManager().beginTransaction().replace(R.id.fl_container,bFragment,"a").addToBackStack(null).commitAllowingStateLoss();
                }
            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                mTvTitle.setText("我是新文字！(  所以需要在BFrament中添加addToBackStack（）方法，但是返回之后这段文字会消失所以需要判断是否有AFragment的tag 如果有就隐藏 然后add BFragment)");
            }
        });

        mBtnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //应用getActivity方法转成ContainerActivity 再调用setData方法；
//                ((ContainerActivity)getActivity()).setData("你好");
                listener.onClick("这个是通过运用AFragment中自定义接口去实现信息的传递 还有一种方法是通过Activity的公共类去调用:(ContainerActivity)getActivity()).setData(“”);");
            }
        });


        //将值发送到mTvTitle中
        if ( getArguments() != null){
            mTvTitle.setText(getArguments().getString("title"));
        }
    }


    //重新构造一个newInstance的方法 用来传递参数
    public  static  AFragment newInstance ( String title){
        // new 一个AFragment，还有Bundle；
        AFragment fragment = new AFragment();
        Bundle bundle = new Bundle();
        //传递的值 用putString 在Activity中接收到的值
        bundle.putString("title",title);
        //被die掉之后还会接着传值
        fragment.setArguments(bundle);
        return fragment;
    }

    /**  创建一个IonMessageClick 在Activity中实现它！
     *
     */
    public interface IonMessageClick{
        void  onClick(String text);
    }


    //    //重新保持关系的时候会调用这个方法
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
////        mActivity  = (Activity) context;
//    }

//脱离了Activity的约束关系 调用这个方法
//    @Override
//    public void onDetach() {
//        super.onDetach();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        //取消异步
//    }

}

package com.chen.activity;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chen.boardcast.NetBroadcastReceiver;
import com.chen.handle.ActivityCollector;
import com.chen.handle.NetUtil;
import com.chen.handle.ScreenUtil;

/*一个重新书写后的活动基类
每当创建新活动时，总是把新活动添加到ActivityCollector的活动列表中，
活动销毁时则移出列表
 */
public class BaseActivity extends AppCompatActivity implements NetBroadcastReceiver.NetEvent {
    public NetBroadcastReceiver netBroadcastReceiver;
    public static NetBroadcastReceiver.NetEvent event;
    private int netMobile;
    private ProgressDialog progressDialog=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event= this;
        //实例化IntentFilter对象
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netBroadcastReceiver = new NetBroadcastReceiver();
        registerReceiver(netBroadcastReceiver, filter);
        ActivityCollector.addActivity(this);
        ScreenUtil.adapterScreen(this,480,false);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(netBroadcastReceiver);
        ActivityCollector.removeActivity(this);
        ScreenUtil.resetScreen(this);
    }

    public boolean inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(BaseActivity.this);
        return isNetConnect();
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;

        }
        return false;
    }

    @Override
    public void onNetChange(int netMobile) {
        if(!inspectNet()){
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("网络异常");
            progressDialog.setMessage("等待网络恢复");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }else{
            if(progressDialog!=null){
                progressDialog.cancel();
            }
        }
    }
}

package com.marton.chat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marton on 16/3/15.
 */
public class MsgAdapter extends BaseAdapter {
    private List<Msg> mMsgList = new ArrayList<Msg>();
    private Context mContext;

    public MsgAdapter(Context context,List<Msg> msgList){
        this.mContext = context;
        mMsgList.addAll(msgList);
    }

    public void addMsg(Msg msg){
        mMsgList.add(msg);
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return mMsgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Msg msg = (Msg)getItem(position);
        ViewHolder vHoler;
        View view;
        if(convertView == null){
            vHoler = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.msg_item,null);
            vHoler.mMsgText = (TextView)view.findViewById(R.id.chat_item_text);
            view.setTag(vHoler);
        }else{
            view = convertView;
            vHoler = (ViewHolder) view.getTag();
        }
        showMsg(vHoler,msg);
        return view;
    }

    private void showMsg(ViewHolder viewHolder,Msg msg){
        LinearLayout.LayoutParams lp = new LinearLayout.
                LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (msg.getType()){
            case Msg.TYPE_MSG_SEND:
                lp.gravity = Gravity.RIGHT;
                viewHolder.mMsgText.setLayoutParams(lp);
                viewHolder.mMsgText.setBackgroundColor(mContext.getResources().
                        getColor(R.color.color_send_bg));
                viewHolder.mMsgText.setText(msg.getContent());
                break;
            case Msg.TYPE_MSG_RECEIVE:
                lp.gravity = Gravity.LEFT;
                viewHolder.mMsgText.setLayoutParams(lp);
                viewHolder.mMsgText.setBackgroundColor(mContext.getResources().
                        getColor(R.color.color_receive_bg));
                viewHolder.mMsgText.setText(msg.getContent());
                break;
            default:
                break;
        }
    }

    @Override
    public int getCount() {
        return mMsgList.size();
    }

    class ViewHolder{
        TextView mMsgText;
    }
}

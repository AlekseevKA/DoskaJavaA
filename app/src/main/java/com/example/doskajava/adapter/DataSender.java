package com.example.doskajava.adapter;

import com.example.doskajava.NewPost;

import java.util.List;

public interface DataSender {

    public void onDataRecived(List<NewPost> listData);
}

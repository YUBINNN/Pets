/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetContract.PetEntry;

/**
 * 用于展示宠物信息
 * ContentResolver参考 ：https://blog.csdn.net/rankun1/article/details/51439574
 */
public class CatalogActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /** Identifier for the pet data loader */
    private static final int PET_LOADER = 0;

    /** 数据适配器 */
    PetCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        //初始化布局
        initView();
        // 异步加载数据
        getLoaderManager().initLoader(PET_LOADER, null, this);
    }

    /**初始化布局*/
    private void initView() {
        // 悬空编辑按钮
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // 宠物列表
        ListView petListView = (ListView) findViewById(R.id.list);

        // 宠物列表为空时显示
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        //宠物列表适配
        mCursorAdapter = new PetCursorAdapter(this, null);
        petListView.setAdapter(mCursorAdapter);

        // 监听宠物列表
        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // 跳转宠物编辑信息编辑活动
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                //获取对应宠物的URL，表的URL+id
                Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);
                //活动之间传递数据
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });
    }

    /**增加一个宠物样板*/
    private void insertPet() {
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrier");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);
        //操作数据库
        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);
    }

    /**删除宠物列表所有宠物*/
    private void deleteAllPets() {
        //CONTENT_URI表示删除所有
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    /** actionBar显示菜单*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    /** actionBar菜单的点击事件*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // 插入一个宠物样板信息
            case R.id.action_insert_dummy_data:
                insertPet();
                return true;
            //删除所有宠物
            case R.id.action_delete_all_entries:
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** 创建一个LOADER*/
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // 定义获取的信息
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,  // 上下文
                PetEntry.CONTENT_URI,   //  要访问数据库的 uri地址
                projection,             // projection ： 对应于数据库语句里的 某列， 如果只需要访问某几列， 则传入这几列的名字即可， 如果不传， 则默认访问全部数据。
                null,                   // 一些特殊的筛选条件，比如要求年龄大于10， 则传入 “age > ?”
                null,                   // 传入具体的参数， 会替换上述 selection中的？
                null);                  // 排序规则， 可以为空
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // 交换一个新的游标，返回旧的游标，适配数据
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // 交换一个新的游标，返回旧的游标
        mCursorAdapter.swapCursor(null);
    }
}

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
package com.example.android.pets.data;

import android.net.Uri;
import android.content.ContentResolver;
import android.provider.BaseColumns;

/**
 * API Contract for the Pets app.
 * 静态数据类，用于操控数据库表的参数
 */
public final class PetContract {

    // 构造
    private PetContract() {}

    /**APP包名 */
    public static final String CONTENT_AUTHORITY = "com.example.android.pets";

    /** URL固定部分+ AUTHORITY*/
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**app 包里的表路径名称 */
    public static final String PATH_PETS = "pets";

    /**定义表名称参数，用于访问数据库表的数据*/
    public static final class PetEntry implements BaseColumns {

        /**
         * 每一个ContentProvider都拥有一个公共的Uri，这个Uri用于表示这个ContentProvider提供的数据
         * withAppendedPath：通过将已编码的路径段附加到基本Uri来创建新的Uri。
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PETS);


         /**列表的宠物*/
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /**列表的某个宠物*/
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PETS;

        /** 数据库表名 */
        public final static String TABLE_NAME = "pets";

        /**行号*/
        public final static String _ID = BaseColumns._ID;

        /**宠物名字项名*/
        public final static String COLUMN_PET_NAME ="name";

        /**宠物血统项名*/
        public final static String COLUMN_PET_BREED = "breed";

        /**宠物性别项名*/
        public final static String COLUMN_PET_GENDER = "gender";

        /**宠物重量项名*/
        public final static String COLUMN_PET_WEIGHT = "weight";

        /**重量选项参数*/
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

        /**判断性别的输入是否正确*/
        public static boolean isValidGender(int gender) {
            if (gender == GENDER_UNKNOWN || gender == GENDER_MALE || gender == GENDER_FEMALE) {
                return true;
            }
            return false;
        }
    }

}


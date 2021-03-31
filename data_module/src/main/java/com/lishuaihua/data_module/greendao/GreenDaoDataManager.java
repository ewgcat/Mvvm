package com.lishuaihua.data_module.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lishuaihua.data_module.greendao.gen.DaoMaster;
import com.lishuaihua.data_module.greendao.gen.DaoSession;
import com.lishuaihua.data_module.model.KeyWordBean;

import java.util.List;


/**
 * 数据库操作统一管理类
 */
public  class GreenDaoDataManager {
    private final static String TAG = GreenDaoDataManager.class.getSimpleName();
    private final static String dbName = "green_db";
    private static GreenDaoDataManager mInstance;
    private MySQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    public DaoSession getmDaoSession() {
        return mDaoSession;
    }

    public GreenDaoDataManager(Context context) {
        openHelper = new MySQLiteOpenHelper(context, dbName, null);
        db = openHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }


    /**
     * 初始化
     * @param context
     */
    public static void init(Context context) {
        if (mInstance == null) {
            synchronized (GreenDaoDataManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoDataManager(context);
                }
            }
        }
    }

    /**
     * 获取单例引用
     */
    public static GreenDaoDataManager getInstance() {
        if (mInstance == null) {
            try {
                throw new Exception("please init dbmanager before use");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mInstance;
    }

    /**
     * 新增 .修改
     * @param keyWordBean
     */
    public void insertKeyWordBean(KeyWordBean keyWordBean){
        List<KeyWordBean> keyWordBeans = queryKeyWordBeans();
        KeyWordBean repeatKeyWordBean=null;
        for (int i = 0; i < keyWordBeans.size(); i++) {
            KeyWordBean keywordBean=keyWordBeans.get(i);
            if (keywordBean.getKeyword().equals(keyWordBean.getKeyword())){
                repeatKeyWordBean=keywordBean;
            }
        }
        if (repeatKeyWordBean!=null){
            removeKeyWordBean(repeatKeyWordBean);
        }
        mDaoSession.getKeyWordBeanDao().insertOrReplace(keyWordBean);
    }

    /**
     * 删全部
     */
    public void clearKeyWordBeans() {
        mDaoSession.getKeyWordBeanDao().deleteAll();
    }

    /**
     * 删单个
     * @param keyWordBean
     */
    public void removeKeyWordBean(KeyWordBean keyWordBean){
        mDaoSession.getKeyWordBeanDao().deleteByKey(keyWordBean.getId());
    }

    /**
     * 查询
     * @return
     */
    public List<KeyWordBean> queryKeyWordBeans(){
        return   mDaoSession.getKeyWordBeanDao().queryBuilder().build().list();
    }

}
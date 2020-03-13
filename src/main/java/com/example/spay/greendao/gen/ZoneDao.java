package com.example.spay.greendao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.spay.greendao.entity.Zone;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "T_Zone".
*/
public class ZoneDao extends AbstractDao<Zone, Long> {

    public static final String TABLENAME = "T_Zone";

    /**
     * Properties of entity Zone.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property ZoneId = new Property(0, long.class, "zoneId", true, "ZoneId");
        public final static Property CityId = new Property(1, int.class, "CityId", false, "CityID");
        public final static Property ZoneName = new Property(2, String.class, "zoneName", false, "ZoneName");
    }


    public ZoneDao(DaoConfig config) {
        super(config);
    }
    
    public ZoneDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Zone entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getZoneId());
        stmt.bindLong(2, entity.getCityId());
 
        String zoneName = entity.getZoneName();
        if (zoneName != null) {
            stmt.bindString(3, zoneName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Zone entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getZoneId());
        stmt.bindLong(2, entity.getCityId());
 
        String zoneName = entity.getZoneName();
        if (zoneName != null) {
            stmt.bindString(3, zoneName);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public Zone readEntity(Cursor cursor, int offset) {
        Zone entity = new Zone( //
            cursor.getLong(offset + 0), // zoneId
            cursor.getInt(offset + 1), // CityId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // zoneName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Zone entity, int offset) {
        entity.setZoneId(cursor.getLong(offset + 0));
        entity.setCityId(cursor.getInt(offset + 1));
        entity.setZoneName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Zone entity, long rowId) {
        entity.setZoneId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Zone entity) {
        if(entity != null) {
            return entity.getZoneId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Zone entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
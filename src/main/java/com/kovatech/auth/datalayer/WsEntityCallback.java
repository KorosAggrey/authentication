package com.kovatech.auth.datalayer;

import org.reactivestreams.Publisher;
import org.springframework.data.r2dbc.mapping.OutboundRow;
import org.springframework.data.r2dbc.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.sql.SqlIdentifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class WsEntityCallback implements BeforeSaveCallback<BaseEntity> {

    @Override
    public Publisher<BaseEntity> onBeforeSave(final BaseEntity entity,
                                              final OutboundRow row,
                                              final SqlIdentifier table) {
        // do something
        return Mono.just(entity);
    }
}

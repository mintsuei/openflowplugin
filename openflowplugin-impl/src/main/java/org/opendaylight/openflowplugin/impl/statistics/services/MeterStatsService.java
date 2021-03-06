/**
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.impl.statistics.services;

import org.opendaylight.openflowplugin.api.openflow.device.DeviceContext;
import org.opendaylight.openflowplugin.api.openflow.device.RequestContextStack;
import org.opendaylight.openflowplugin.api.openflow.device.Xid;
import org.opendaylight.openflowplugin.impl.services.AbstractSimpleService;
import org.opendaylight.openflowplugin.impl.services.RequestInputUtils;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.statistics.rev131111.GetMeterStatisticsInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.statistics.rev131111.GetMeterStatisticsOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.MeterId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.MultipartType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.MultipartRequestInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.OfHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.MultipartRequestMeterCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.multipart.request.meter._case.MultipartRequestMeterBuilder;

final class MeterStatsService extends AbstractSimpleService<GetMeterStatisticsInput, GetMeterStatisticsOutput> {

    MeterStatsService(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        super(requestContextStack, deviceContext, GetMeterStatisticsOutput.class);
    }

    @Override
    protected OfHeader buildRequest(final Xid xid, final GetMeterStatisticsInput input) {
        MultipartRequestMeterCaseBuilder caseBuilder =
                new MultipartRequestMeterCaseBuilder();
        MultipartRequestMeterBuilder mprMeterBuild =
                new MultipartRequestMeterBuilder();
        mprMeterBuild.setMeterId(new MeterId(input.getMeterId().getValue()));
        caseBuilder.setMultipartRequestMeter(mprMeterBuild.build());

        MultipartRequestInputBuilder mprInput =
                RequestInputUtils.createMultipartHeader(MultipartType.OFPMPMETER, xid.getValue(), getVersion());
        mprInput.setMultipartRequestBody(caseBuilder.build());
        return mprInput.build();
    }
}

/**
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.impl.statistics.services;

import org.opendaylight.openflowplugin.api.OFConstants;
import org.opendaylight.openflowplugin.api.openflow.device.DeviceContext;
import org.opendaylight.openflowplugin.api.openflow.device.RequestContextStack;
import org.opendaylight.openflowplugin.api.openflow.device.Xid;
import org.opendaylight.openflowplugin.impl.services.AbstractSimpleService;
import org.opendaylight.openflowplugin.impl.services.RequestInputUtils;
import org.opendaylight.openflowplugin.openflow.md.util.FlowCreatorUtil;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.statistics.rev130819.GetAllFlowStatisticsFromFlowTableInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.statistics.rev130819.GetAllFlowStatisticsFromFlowTableOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.MultipartType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.MultipartRequestInputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.OfHeader;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.MultipartRequestFlowCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.protocol.rev130731.multipart.request.multipart.request.body.multipart.request.flow._case.MultipartRequestFlowBuilder;

public class AllFlowsInTableService extends AbstractSimpleService<GetAllFlowStatisticsFromFlowTableInput, GetAllFlowStatisticsFromFlowTableOutput> {

    public AllFlowsInTableService(final RequestContextStack requestContextStack, final DeviceContext deviceContext) {
        super(requestContextStack, deviceContext, GetAllFlowStatisticsFromFlowTableOutput.class);
    }

    @Override
    protected OfHeader buildRequest(final Xid xid, final GetAllFlowStatisticsFromFlowTableInput input) {
        final MultipartRequestFlowBuilder mprFlowRequestBuilder = new MultipartRequestFlowBuilder();
        mprFlowRequestBuilder.setTableId(input.getTableId().getValue());
        mprFlowRequestBuilder.setOutPort(OFConstants.OFPP_ANY);
        mprFlowRequestBuilder.setOutGroup(OFConstants.OFPG_ANY);
        mprFlowRequestBuilder.setCookie(OFConstants.DEFAULT_COOKIE);
        mprFlowRequestBuilder.setCookieMask(OFConstants.DEFAULT_COOKIE_MASK);

        final short version = getVersion();
        FlowCreatorUtil.setWildcardedFlowMatch(version, mprFlowRequestBuilder);

        final MultipartRequestFlowCaseBuilder multipartRequestFlowCaseBuilder = new MultipartRequestFlowCaseBuilder();
        multipartRequestFlowCaseBuilder.setMultipartRequestFlow(mprFlowRequestBuilder.build());

        final MultipartRequestInputBuilder mprInput = RequestInputUtils.createMultipartHeader(
                MultipartType.OFPMPFLOW, xid.getValue(), version);

        mprInput.setMultipartRequestBody(multipartRequestFlowCaseBuilder.build());

        return mprInput.build();
    }
}

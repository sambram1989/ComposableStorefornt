package com.wholescripts.core.facade.impl;

import com.wholescripts.core.facade.WebBannersAndScriptsFacade;
import com.wholescripts.core.model.WebBannersAndScriptsModel;
import com.wholescripts.core.service.WebBannersAndScriptsService;
import com.wholescripts.dto.WebBannersAndScriptsData;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import javax.annotation.Resource;

public class WebBannersAndScriptsFacadeImpl implements WebBannersAndScriptsFacade {
    @Resource(name = "webBannersAndScriptsService")
    private WebBannersAndScriptsService webBannersAndScriptsService;

    @Resource(name = "webBannersAndScriptsDataConverter")
    private Converter<WebBannersAndScriptsModel, WebBannersAndScriptsData> webBannersAndScriptsDataConverter;

    @Override
    public WebBannersAndScriptsData getAllWebBannersAndScripts() {
        WebBannersAndScriptsModel model = webBannersAndScriptsService.getAllWebBannersAndScripts();
        return webBannersAndScriptsDataConverter.convert(model);
    }
}

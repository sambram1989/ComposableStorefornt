package com.wholescripts.core.service.impl;

import com.wholescripts.core.dao.WebBannersAndScriptsDao;
import com.wholescripts.core.model.WebBannersAndScriptsModel;
import com.wholescripts.core.service.WebBannersAndScriptsService;

import javax.annotation.Resource;

/**
 * @author Sadiq
 */
public class WebBannersAndScriptsServiceImpl implements WebBannersAndScriptsService {
    @Resource(name = "webBannersAndScriptsDao")
    private WebBannersAndScriptsDao webBannersAndScriptsDao;
    @Override
    public WebBannersAndScriptsModel getAllWebBannersAndScripts() {
        return webBannersAndScriptsDao.getAllWebBannersAndScripts();
    }
}

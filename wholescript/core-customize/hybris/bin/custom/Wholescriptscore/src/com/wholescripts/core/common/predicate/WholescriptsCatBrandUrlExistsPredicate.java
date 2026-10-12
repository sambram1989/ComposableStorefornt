package com.wholescripts.core.common.predicate;

import com.wholescripts.core.service.WholescriptsCategoryService;
import com.wholescripts.core.service.WholescriptsCoreService;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.function.Predicate;

public class WholescriptsCatBrandUrlExistsPredicate implements Predicate<String> {
    @Resource(name = "wholescriptsCategoryService")
    private WholescriptsCategoryService wholescriptsCategoryService;
    @Resource(name = "wholescriptsCoreService")
    private WholescriptsCoreService wholescriptsCoreService;

    @Override
    public boolean test(String url) {
        try {
            if (StringUtils.equals("/catalog", url) || StringUtils.containsAny("/category", url) && !url.contains("/brand")) {
                wholescriptsCategoryService.getCategoryForUrl(url);
                return true;
            } else if (StringUtils.isNotEmpty(url) && StringUtils.containsAny("/brand", url))
                wholescriptsCoreService.getBrandCodeByBrandUrl(url);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}

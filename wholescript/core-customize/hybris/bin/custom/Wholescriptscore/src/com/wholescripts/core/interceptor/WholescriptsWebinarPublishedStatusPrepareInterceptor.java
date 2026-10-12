package com.wholescripts.core.interceptor;

import com.wholescripts.core.enums.VideoStatus;
import com.wholescripts.core.model.WebinarModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WholescriptsWebinarPublishedStatusPrepareInterceptor implements PrepareInterceptor {
    @Override
    public void onPrepare(Object model, InterceptorContext interceptorContext) throws InterceptorException {
               if (model instanceof WebinarModel webinarModel)
        {
            if(webinarModel.getVideoStatus().equals(VideoStatus.PUBLISHED))

            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(new Date());
                webinarModel.setPublishedDate(formattedDate);
            }
        }

    }
}

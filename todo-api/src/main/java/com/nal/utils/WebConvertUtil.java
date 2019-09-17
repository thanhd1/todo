package com.nal.utils;

import com.nal.core.entity.AbstractEntity;
import com.nal.form.AbstractForm;
import org.springframework.beans.BeanUtils;

public class WebConvertUtil {

    public static <T extends AbstractEntity> T formToEntity(AbstractForm src, T dest, String... ignoreProperties) {
        BeanUtils.copyProperties(src, dest, ignoreProperties);
        return dest;
    }

}

package com.enewschamp.article.page.data;

import java.time.LocalDateTime;

import com.enewschamp.app.common.AbstractDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class PropertyAuditData  extends AbstractDTO {

	private static final long serialVersionUID = 7733232790944195014L;
	private String userId;
	private LocalDateTime date;
	private String value;
}

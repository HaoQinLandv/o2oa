package com.x.processplatform.assemble.surface.jaxrs.task;

import org.apache.commons.lang3.StringUtils;

import com.google.gson.JsonElement;
import com.x.base.core.container.EntityManagerContainer;
import com.x.base.core.container.factory.EntityManagerContainerFactory;
import com.x.base.core.project.Applications;
import com.x.base.core.project.x_processplatform_service_processing;
import com.x.base.core.project.exception.ExceptionAccessDenied;
import com.x.base.core.project.exception.ExceptionEntityNotExist;
import com.x.base.core.project.http.ActionResult;
import com.x.base.core.project.http.EffectivePerson;
import com.x.base.core.project.jaxrs.WoId;
import com.x.processplatform.assemble.surface.Business;
import com.x.processplatform.assemble.surface.ThisApplication;
import com.x.processplatform.core.entity.content.Task;
import com.x.processplatform.core.entity.element.Application;

class ActionManageCompleted extends BaseAction {

	ActionResult<Wo> execute(EffectivePerson effectivePerson, String id, JsonElement jsonElement) throws Exception {
		try (EntityManagerContainer emc = EntityManagerContainerFactory.instance().create()) {
			ActionResult<Wo> result = new ActionResult<>();
			Wi wi = this.convertToWrapIn(jsonElement, Wi.class);
			Task task = null;
			Business business = new Business(emc);
			task = emc.find(id, Task.class);
			if (null == task) {
				throw new ExceptionEntityNotExist(id,Task.class);
			}
			Application application = business.application().pick(task.getApplication());
			if (null == application) {
				throw new ExceptionEntityNotExist(task.getApplication(), Application.class);
			}
			// 需要对这个应用的管理权限
			emc.beginTransaction(Task.class);
			if (!business.application().allowControl(effectivePerson, application)) {
				throw new ExceptionAccessDenied(effectivePerson, task);
			}
			/* 如果有输入新的路由决策覆盖原有决策 */
			if (StringUtils.isNotEmpty(wi.getRouteName())) {
				task.setRouteName(wi.getRouteName());
			}
			/* 如果有新的流程意见那么覆盖原有流程意见 */
			if (StringUtils.isNotEmpty(wi.getOpinion())) {
				task.setOpinion(wi.getOpinion());
			}
			emc.commit();
			/** 20180319 */
			// ThisApplication.context().applications().putQuery(x_processplatform_service_processing.class,
			// "task/" + URLEncoder.encode(task.getId(), DefaultCharset.name) +
			// "/completed", null);
			ThisApplication.context().applications().putQuery(x_processplatform_service_processing.class,
					Applications.joinQueryUri("task", task.getId(), "processing"), null);
			Wo wo = new Wo();
			wo.setId(task.getId());
			result.setData(wo);
			return result;
		}
	}

	public static class Wi extends Task {

		private static final long serialVersionUID = -4726539076530209219L;

	}

	public static class Wo extends WoId {
	}

}

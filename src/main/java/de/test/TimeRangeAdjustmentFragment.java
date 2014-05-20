package de.test;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.yui.calendar.TimeField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ImageButton;
import org.apache.wicket.markup.html.image.ContextPathGenerator;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.cycle.RequestCycle;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Matthias Drummer on 20.05.2014.
 */
public class TimeRangeAdjustmentFragment extends Fragment {

    private static final long serialVersionUID = 5311962913404766161L;

    private final Set<FragmentCallback> fragmentCallbacks = new HashSet<FragmentCallback>();
    private final int callerId;

    public TimeRangeAdjustmentFragment(String id, String markupId, MarkupContainer markupProvider, IModel<FragmentModel> model, int callerId) {
        super(id, markupId, markupProvider, model);

        this.callerId = callerId;
    }

    public void addCallback(FragmentCallback cb) {
        fragmentCallbacks.add(cb);
    }

    public void removeCallback(FragmentCallback cb) {
        fragmentCallbacks.remove(cb);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        setOutputMarkupId(true);

        Form<FragmentModel> form = new Form<FragmentModel>("fragmentForm");
        form.add(new Label("label", new PropertyModel<String>(getDefaultModelObject(), "labelText")));
//        form.add(new AjaxButton("submitBtn") {
//            @Override
//            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
//                super.onSubmit(target, form);
//
//                for(FragmentCallback cb : fragmentCallbacks) {
//                    cb.call(target, callerId);
//                }
//            }
//        });
        ContextPathGenerator gen = new ContextPathGenerator("images/close.png");

        ImageButton sb = new ImageButton("button", "");
        sb.add(new AjaxFormSubmitBehavior("onClick") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                for (FragmentCallback cb : fragmentCallbacks) {
                    cb.call(target, callerId);
                }
            }
        });
        sb.add(gen);

        form.add(new CustomTimeField("timeField", new PropertyModel<Date>(getDefaultModelObject(), "timeFieldValue")));

        form.add(sb);


        add(form);
    }

    public static class FragmentModel implements Serializable {

        private static final long serialVersionUID = 1164004115653440551L;
        private String labelText = "defaultText";
        private Date timeFieldValue = new Date();

        public String getLabelText() {
            return labelText;
        }

        public void setLabelText(String labelText) {
            this.labelText = labelText;
        }

        public void setTimeFieldValue(Date timeFieldValue) {
            this.timeFieldValue = timeFieldValue;
        }

        public Date getTimeFieldValue() {
            return timeFieldValue;
        }
    }

    public static interface FragmentCallback extends Serializable {

        public void call(AjaxRequestTarget target, int callerId);
    }

    public static class CustomTimeField extends TimeField {

        private static final long serialVersionUID = -459325788945543096L;

        public CustomTimeField(String id, IModel<Date> model) {
            super(id, model);
        }

        @Override
        protected boolean use12HourFormat() {
            return false;
        }
    }
}

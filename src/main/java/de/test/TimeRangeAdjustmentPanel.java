package de.test;

import de.test.TimeRangeAdjustmentFragment.FragmentModel;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.Calendar;

/**
 * Created by Matthias Drummer on 20.05.2014.
 */
public class TimeRangeAdjustmentPanel extends Panel {

    private static final long serialVersionUID = 7650939011077832204L;
    private int count = 0;

    public TimeRangeAdjustmentPanel(String id) {
        super(id);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final TimeRangeAdjustmentFragment frag1 = new TimeRangeAdjustmentFragment("frag1", "frag", this, Model.of(new FragmentModel()), 1);
        final TimeRangeAdjustmentFragment frag2 = new TimeRangeAdjustmentFragment("frag2", "frag", this, Model.of(new FragmentModel()), 2);

        frag1.addCallback(new TimeRangeAdjustmentFragment.FragmentCallback() {
            @Override
            public void call(AjaxRequestTarget target, int callerId) {
                if(callerId == 1) {

                    // manipulate models
                    FragmentModel defaultModelObject = (FragmentModel) frag1.getDefaultModelObject();
                    count++;
                    defaultModelObject.setLabelText("text: " + count);
                    System.out.println("Date: " + defaultModelObject.getTimeFieldValue());

//                    Calendar cal = Calendar.getInstance();
//                    cal.set(Calendar.HOUR_OF_DAY, 11);
//                    defaultModelObject.getTimeFieldValue().setTime(cal.getTimeInMillis());

                    FragmentModel frag2Model = (FragmentModel) frag2.getDefaultModelObject();
                    frag2Model.setLabelText("f2Text: " + count);
//                    frag2Model.getTimeFieldValue().setTime(cal.getTimeInMillis());
                    frag2Model.setTimeFieldValue(defaultModelObject.getTimeFieldValue());

                    target.add(frag1);
                    target.add(frag2);
                }
            }
        });

        add(frag1);
        add(frag2);
    }
}

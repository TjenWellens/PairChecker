package eu.tjenwellens.pairtester.groups;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import eu.tjenwellens.pairtester.activities.ListsActivity;

/**
 *
 * @author Tjen
 */
public class ListPanel extends LinearLayout implements Group
{
    private TextView tvName;
    private Button btnSize; 
//    private Button btnScore;
    private Group group;
    private ListsActivity groupPanelHandler;

    public ListPanel(ListsActivity context, Group group)
    {
        super(context);
        this.groupPanelHandler = context;
        this.group = group;
        initGUI(context);
    }

    private void initGUI(Context context)
    {
        setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 5, 10, 5);
        setLayoutParams(lp);
        setBackgroundColor(Color.LTGRAY);
        {
            // name
            tvName = new TextView(context);
            tvName.setTextColor(Color.BLACK);
            tvName.setText(" " + group.getName());
            tvName.setClickable(true);
            tvName.setOnClickListener(new OnClickListener()
            {
                public void onClick(View arg0)
                {
                    groupPanelHandler.shortClick(ListPanel.this);
                }
            });
            tvName.setLongClickable(true);
            tvName.setOnLongClickListener(new OnLongClickListener()
            {
                public boolean onLongClick(View arg0)
                {
                    groupPanelHandler.longClick(ListPanel.this);
                    return true;
                }
            });
            // size
            btnSize = new Button(context);
            btnSize.setVisibility(View.INVISIBLE);
//            btnSize.setText("(" + group.getSize() + ")");
//            btnSize.setClickable(true);
//            btnSize.setOnClickListener(new OnClickListener()
//            {
//                public void onClick(View arg0)
//                {
//                    Toast.makeText(ListPanel.this.groupPanelHandler, "Progress: " + group.getProgress() + "%", Toast.LENGTH_SHORT).show();
//                }
//            });
//            // score
//            btnScore = new Button(context);
//            btnScore.setText("" + group.getScore());
//            btnScore.setClickable(true);
//            btnScore.setOnClickListener(new OnClickListener()
//            {
//                public void onClick(View arg0)
//                {
//                    Toast.makeText(ListPanel.this.groupPanelHandler, "Correct: " + group.getCorrects() + "\nWrong: " + group.getWrongs() + "\nSkipped: " + group.getSkips(), Toast.LENGTH_LONG).show();
//                }
//            });

            // add to gui
            this.addView(tvName, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1f));
            this.addView(btnSize, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//            this.addView(btnScore, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    public int getGroupId()
    {
        return group.getGroupId();
    }

    public String getName()
    {
        return group.getName();
    }

    public int getSize()
    {
        return group.getSize();
    }

    public int getScore()
    {
        return group.getScore();
    }

    public int getCorrects()
    {
        return group.getCorrects();
    }

    public int getWrongs()
    {
        return group.getWrongs();
    }

    public int getSkips()
    {
        return group.getSkips();
    }

    public int getProgress()
    {
        return group.getProgress();
    }

    public void setGroupId(int id)
    {
        group.setGroupId(id);
    }
}

package hr.fer.oprpp1.java.gui.layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class CalcLayout represents a custom layout used for calculator program.
 * @author leokiparje
 *
 */

public class CalcLayout implements LayoutManager2 {
	
	private Map<RCPosition, Component> components = new HashMap<>(); 
	
	int space;

	public CalcLayout() {
		space = 0;
	}
	
	public CalcLayout(int spaceSize) {
		space = spaceSize;
	}
	
	@Override
	public void addLayoutComponent(String name, Component comp) {
		
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		
		for (Map.Entry<RCPosition, Component> entries : components.entrySet()) {
			
			if (entries.getValue().equals(comp)) {
				components.remove(entries.getKey());
				return;
			}
			
		}
	
	}
	
	public Dimension LayoutSize(Container parent, String id) {
		
		int maxWidth = 0;
		int maxHeight = 0;
		
		int width = 0;
		int height= 0;
		
		for (Map.Entry<RCPosition, Component> entries : components.entrySet()){
			
			switch(id) {
				case "pref":
					width = entries.getValue().getPreferredSize().width;
					height = entries.getValue().getPreferredSize().height;
					break;
				case "min":
					width = entries.getValue().getMaximumSize().width;
					height = entries.getValue().getMaximumSize().height;
					break;
				case "max":
					width = entries.getValue().getMinimumSize().width;
					height = entries.getValue().getMinimumSize().height;
			}
			
			if (entries.getKey().getRow()==1 && entries.getKey().getColumn()==1) {
				width = (width - 4*space) / 5;
			}
		
			if (width > maxWidth) {
				maxWidth = width;
			}
			if (height > maxHeight) {
				maxHeight = height;
			}
			
		}
		
		Insets insets = parent.getInsets();
		
		return new Dimension (7*maxWidth + 6*space + insets.right + insets.left, 5*maxHeight + 4*space + insets.top + insets.bottom);
		
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		
		return LayoutSize(parent, "pref");
	}

	@Override
	public Dimension minimumLayoutSize(Container parent) {
		
		return LayoutSize(parent, "min");
		
	}

	@Override
	public void layoutContainer(Container parent) {
		
		Insets insets = parent.getInsets();
		
		int calcWidth = parent.getWidth()-6*space-insets.right-insets.left;
		int calcHeight = parent.getHeight()-4*space-insets.top-insets.bottom;
		
		double width = calcWidth / 7;
		double height = calcHeight / 5;
		
		int[] widths = new int[7];
		Arrays.fill(widths, (int)width);
		int[] heights = new int[5];
		Arrays.fill(heights, (int)height);
		
		int index = 0;
		
		while (calcWidth % (int) width != 0) {
			widths[index]+=1;
			if (index==6) {
				index = -1;
			}
			calcWidth--;
			index+=2;
		}
		index = 0;
		while (calcHeight % (int) height != 0) {
			heights[index]+=1;
			if (index==4) {
				index = -1;
			}
			calcHeight--;
			index+=2;
		}
		
		for (Map.Entry<RCPosition, Component> entries : components.entrySet()) {
			
			int row = entries.getKey().getRow();
			int column = entries.getKey().getColumn();
			Component component = entries.getValue();
			
			if (row==1 && column==1) {
				component.setBounds(0+insets.left, 0+insets.top, widths[0]+widths[1]+widths[2]+widths[3]+widths[4]+4*space, heights[0]);
				continue;
			}
			int w=0;
			for (int i=0;i<column-1;i++) {
				w+=widths[i];
			}
			int r=0;
			for (int i=0;i<row-1;i++) {
				r+=heights[i];
			}
			
			component.setBounds(insets.left+w+(column-1)*space, insets.top+r+(row-1)*space, widths[column-1], heights[row-1]);
			
		}
		
	}

	@Override
	public void addLayoutComponent(Component comp, Object constraints) {
		
		if (constraints instanceof RCPosition) {
			
			RCPosition position = (RCPosition) constraints;
			if (components.keySet().contains(position)) throw new CalcLayoutException("Component on specified position already exists.");
			components.put(position, comp);
			
		}else if(constraints instanceof String) {
			
			RCPosition position = RCPosition.parse((String) constraints);
			if (components.keySet().contains(position)) throw new CalcLayoutException("Component on specified position already exists.");
			components.put(position, comp);
			
		}else throw new CalcLayoutException("Second argument of add function should be a position.");
		
	}

	@Override
	public Dimension maximumLayoutSize(Container target) {
		
		return LayoutSize(target, "max");
		
	}

	@Override
	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	@Override
	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	@Override
	public void invalidateLayout(Container target) {
	}
}

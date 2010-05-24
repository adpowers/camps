package edu.washington.cs.cse403.camps.admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.event.*;

/**A user interface for a MapViewer of a MapModel*/
public class MapController{
	//instance variables for display and drawing
	private JFrame frame;
	private MapModel model;
	private MapViewer view;
	private final int clickRadius = 5;

	private PathType chosenPathType;
	private boolean chosenBiDi;

	private JPanel holdsViewsAndButtons;

	//for pathfinding, adding edges
	//manipulating these variables gets ugly because of the possibilities of click combinations
	private boolean startHasBeenSelected = false;
	private boolean actionPerformedWithStartEndPair = false;

	//instance variables for editing
	private String action;
	//private boolean adding = false;
	//private boolean editing = false;
	//private boolean removing = false;
	private MapButtons availableButtons;

	//place to tell the user useful info
	private JTextArea statusText = new JTextArea(5, 80);

	/**Create a new controller
	 * @param model the model to control
	 */
	public MapController(MapModel model){
		this.model = model;
		init();
	}
	public void init(){
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame = new JFrame("Map");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setJMenuBar((new MapMenuBar()).createMenuBar());
		view = new MapViewer(model);

		view.addMouseListener(new MapMouseListener());
		MapMouseMotionListener temp = new MapMouseMotionListener();
		view.addMouseListener(temp);
		view.addMouseMotionListener(temp);
		view.setFocusable(true);
		view.addKeyListener(new MapKeyListener());

		model.add(view);

		holdsViewsAndButtons = new JPanel();
		holdsViewsAndButtons.setLayout(new BorderLayout());

		statusText.setText(model.toString());
		statusText.setLineWrap(true);
		statusText.setWrapStyleWord(true);
		statusText.setEditable(false);
		JScrollPane statusTextScroller = new JScrollPane(statusText);
		holdsViewsAndButtons.add(statusTextScroller, BorderLayout.NORTH);

		holdsViewsAndButtons.add(view, BorderLayout.CENTER);

		JPanel buttonsAndCheckboxes = new JPanel();
		buttonsAndCheckboxes.setLayout(new BorderLayout());
		availableButtons = new MapButtons();
		buttonsAndCheckboxes.add(availableButtons, BorderLayout.CENTER);
		buttonsAndCheckboxes.add(new MapRadioButtons(), BorderLayout.WEST);
		buttonsAndCheckboxes.add(new MapCheckBoxes(), BorderLayout.EAST);

		holdsViewsAndButtons.add(buttonsAndCheckboxes, BorderLayout.SOUTH);

		frame.getContentPane().add(holdsViewsAndButtons, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}

	public void clear(){//React to model data being cleared
		//A few objects are stored here that refer to model data that is gone, so we need to restart
		frame.dispose();
		init();
	}

	/**Reacts to mouse clicks on the viewer by selecting nodes at those locations.*/
	class MapMouseListener extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			//find the node the user clicked on 
			int lowX = e.getX() -clickRadius - view.getXOffset();
			int highX = e.getX() +clickRadius- view.getXOffset();
			int lowY = e.getY() -clickRadius- view.getYOffset();
			int highY = e.getY() +clickRadius- view.getYOffset();
			Node clicked = model.getNode(lowX, highX, lowY, highY);

			//do something with that node
			if(action.equals("Adding Nodes")){
				//ask if the user wants to add a node where they clicked.
				//int addNode = JOptionPane.showConfirmDialog(null,"You did not click on a node, would you like to add one?", "Add a node?", JOptionPane.YES_NO_OPTION);
				//if (addNode == JOptionPane.YES_OPTION){
				//String name = "";
				//while (name.length() < 1 || name.indexOf('|') != -1 || name.indexOf(',') != -1){
				//  name = JOptionPane.showInputDialog("Name the new Node");
				//}

				try{
					model.add(model.data.createNode(e.getX()- view.getXOffset(), e.getY() - view.getYOffset()));
					statusText.append("\nAdded new node");
				}catch (Exception E){
					System.err.println(E);
				}
			}else if(action.equals("Removing")){
				if (clicked != null){
					int removeNode = JOptionPane.showConfirmDialog(null,"You have clicked on a node, would you like to delete it?", "Remove a node?", JOptionPane.YES_NO_OPTION);
					if (removeNode == JOptionPane.YES_OPTION){
						model.removeNode(clicked);
					}
				}
			}else if (action.equals("Adding Buildings")){
				String name = "";
				while (name.length() < 1 || name.indexOf('|') != -1 || name.indexOf(',') != -1){
					name = JOptionPane.showInputDialog("Full Name of the new Building");
				}
				String abbrev = "";
				while (abbrev.length() < 1 || abbrev.indexOf('|') != -1 || abbrev.indexOf(',') != -1){
					abbrev = JOptionPane.showInputDialog("Abbreviation of the new Building");
				}
				try{
					model.add(model.data.createBuilding(name,abbrev,e.getX()- view.getXOffset(), e.getY() - view.getYOffset()));
					statusText.append("\nAdded new building");
				}catch (Exception E){
					System.err.println(E);
				}
			}else if (action.equals("Editing")){
				if (clicked != null && !startHasBeenSelected){
					if(actionPerformedWithStartEndPair){ //clear them so we can do something else
						model.reset();
						actionPerformedWithStartEndPair = false;
					}
					//System.out.println("Reset start and end because of previous action");				  
					model.setStart(clicked);
					startHasBeenSelected = true;
					statusText.append("\nStart selected as " + clicked);
				}else if (clicked != null){
					model.setEnd(clicked);
					actionPerformedWithStartEndPair = true;
					startHasBeenSelected = false;
					statusText.append("\nEnd selected as " + clicked);

					ModifyEdgePopUp popup = new ModifyEdgePopUp(model.getStart(),model.getEnd());
					Point cordinates = view.getLocationOnScreen();
					popup.pack();
					popup.setVisible(true);
					popup.centerOKButtonAt(cordinates.x+e.getX(),cordinates.y+e.getY());
				}
			}else if (action.equals("Move Node")){
				if (clicked != null){
					model.reset();
					model.setStart(clicked);
					startHasBeenSelected = true;
					statusText.append("\nNode for moving selected as " + clicked);
					view.requestFocusInWindow();
				}
			}
		}
	}
	
	class MapKeyListener implements KeyListener{
		public void keyPressed(KeyEvent e) {
			if (action.equals("Move Node")){
				if(e.getKeyCode() == KeyEvent.VK_DOWN){
					Node selected = model.getStart();
					if(selected != null){
						model.modifyNode(selected, selected.getxLocation(), selected.getyLocation()+1);
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_UP){
					Node selected = model.getStart();
					if(selected != null){
						model.modifyNode(selected, selected.getxLocation(), selected.getyLocation()-1);
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_LEFT){
					Node selected = model.getStart();
					if(selected != null){
						model.modifyNode(selected, selected.getxLocation()-1, selected.getyLocation());
					}
				}
				if(e.getKeyCode() == KeyEvent.VK_RIGHT){
					Node selected = model.getStart();
					if(selected != null){
						model.modifyNode(selected, selected.getxLocation()+1, selected.getyLocation());
					}
				}
			}
		}
		public void keyReleased(KeyEvent e){
			
		}
		public void keyTyped(KeyEvent e){
			
		}
	}

	/**Reacts to mouse drags to make large maps movable*/
	class MapMouseMotionListener extends MouseInputAdapter{
		private volatile MouseEvent last; //to calculate the distance the mouse was dragged to update the view
		public void mouseDragged(MouseEvent e){
			if(SwingUtilities.isRightMouseButton(e)){
				if(last == null){
					last = e;
				}else{
					int deltaX = last.getX() - e.getX();
					int deltaY = last.getY() - e.getY();
					view.changeXOffset(-deltaX);
					view.changeYOffset(-deltaY);
					last = e;
				}
			}

		}
		public void mouseReleased(MouseEvent e) {
			if(SwingUtilities.isRightMouseButton(e)){
				last = null;
			}
		}
	}

	/**Buttons for this controller*/
	class MapButtons extends JPanel implements ActionListener{
		//JButton randomAlgorithm;
		//JButton addLocation;
		JButton reset;
		JButton refresh;


		int algorithmToUse = 1; //1 is dijkstra, 0 is random

		/**Create the buttons this controller needs*/
		public MapButtons(){
			//randomAlgorithm = new JButton("Find Path");
			//addLocation = new JButton("Add Location");
			refresh = new JButton("Refresh Data");
			reset = new JButton("Reset");

			//randomAlgorithm.addActionListener(this);
			refresh.addActionListener(this);
			reset.addActionListener(this);

			//add(randomAlgorithm);
			//add(addLocation);
			add(refresh);
			add(reset);

			setAction("Editing");
		}

		/**Process button presses*/
		public void actionPerformed(ActionEvent e){
			if(e.getActionCommand().equals("Reset")){
				model.reset();
				startHasBeenSelected = false;
				actionPerformedWithStartEndPair = false;
			}else if(e.getActionCommand().equals("Refresh Data")){
				model.clear();
				model.data.fillData(model);
				clear();
			}
		}
		public void setAction(String myAction){
			model.reset();
			actionPerformedWithStartEndPair = false;
			startHasBeenSelected = false;
			action = myAction;
			/*
    	if(action.equals("Editing")){
    		addEdge.setEnabled(true);
    		removeEdge.setEnabled(true);
    	}else{
    		addEdge.setEnabled(false);
    		removeEdge.setEnabled(false);
    	}
			 */
		}

		/**swaps which algorithm the Find Path button uses. 
		 * @param algorithmToUse 2 is closerTo, 1 is dijkstra, 0 is random.*/
		public void setAlgorithm(int algorithmToUse){
			this.algorithmToUse = algorithmToUse;
		}
	}

	/**Checkboxes used in this controller*/
	class MapCheckBoxes extends JPanel implements ItemListener{
		JCheckBox showPicture;
		JCheckBox showNonBuildingDestinations;

		/**Creates the checkboxes we need*/
		public MapCheckBoxes(){
			setLayout(new GridLayout(2, 1));

			showPicture = new JCheckBox("Show Picture", true);
			showPicture.addItemListener(this);

			showNonBuildingDestinations = new JCheckBox("Non-Building Destinations", true);
			showNonBuildingDestinations.addItemListener(this);

			add(showPicture);
			add(showNonBuildingDestinations);
		}

		/**Listens to checkboxes*/
		//http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4/CheckBoxDemo.java
		public void itemStateChanged(ItemEvent e){
			Object sourceButton = e.getItemSelectable();
			if(e.getStateChange() == ItemEvent.SELECTED && sourceButton == showPicture){
				view.setShowPicture(true);
			} else if(e.getStateChange() == ItemEvent.DESELECTED && sourceButton == showPicture){
				view.setShowPicture(false);
			} else if(e.getStateChange() == ItemEvent.SELECTED && sourceButton == showNonBuildingDestinations){
				view.setShowNonBuildings(true);
			} else if(e.getStateChange() == ItemEvent.DESELECTED && sourceButton == showNonBuildingDestinations){
				view.setShowNonBuildings(false);
			}
		}
	}

	class ModifyEdgePopUp extends JWindow implements ActionListener{
		//private JLabel bidirectionalText;
		private Node start;
		private Node end;
		private Edge between;
		private JCheckBox bidirectional;
		private JLabel pathTypeslabel;
		private JComboBox pathTypes;
		private JButton ok;
		private JButton cancel;
		private JButton remove;

		public ModifyEdgePopUp(Node a, Node b){
			//this.setUndecorated(true);
			this.start = a;
			this.end = b;

			this.setAlwaysOnTop(true);
			this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
			//bidirectionalText = new JLabel("Bi-directional?");
			bidirectional = new JCheckBox("Bi-directional?");
			pathTypeslabel = new JLabel("Path Type");
			pathTypes = new JComboBox();
			ok = new JButton("OK");
			cancel = new JButton("Cancel");
			Iterator<PathType> iter = model.getPathTypes().iterator();
			while(iter.hasNext()){
				PathType cur = iter.next();
				pathTypes.addItem(cur);
			}
			ok.addActionListener(this);
			cancel.addActionListener(this);
			JPanel buttons = new JPanel(new BorderLayout());
			buttons.add(ok,BorderLayout.WEST);
			buttons.add(Box.createRigidArea(new Dimension(5, 0)),BorderLayout.CENTER);
			buttons.add(cancel,BorderLayout.EAST);


			between = a.getExit(b);
			JLabel top;
			if(between != null){
				top = new JLabel("Modify Edge");
				bidirectional.setSelected(between.isBidirectional());
				pathTypes.setSelectedItem(between.getPathType());
			}else{
				top = new JLabel("Add Edge");
				bidirectional.setSelected(chosenBiDi);
				pathTypes.setSelectedItem(chosenPathType);
			}
			bidirectional.setAlignmentX(Component.LEFT_ALIGNMENT);
			pathTypeslabel.setAlignmentX(Component.LEFT_ALIGNMENT);
			pathTypes.setAlignmentX(Component.LEFT_ALIGNMENT);
			buttons.setAlignmentX(Component.LEFT_ALIGNMENT);

			this.getContentPane().add(top);
			this.getContentPane().add(bidirectional);
			this.getContentPane().add(pathTypeslabel);
			this.getContentPane().add(pathTypes);
			this.getContentPane().add(buttons);
			if(between != null){
				remove = new JButton("Remove Edge");
				remove.addActionListener(this);
				this.getContentPane().add(remove);
			}
		}

		public void actionPerformed(ActionEvent e){
			if(e.getActionCommand().equals("Cancel")){
				dispose();

			}else if(e.getActionCommand().equals("OK")){
				if (start != null && end != null){
					PathType chosen = (PathType)(pathTypes.getSelectedItem());
					if(chosen != null){
						if(between != null){
							model.removeEdge(between);
						}
						model.createEdge(start, end, chosen, bidirectional.isSelected());
					}
				} else {
					statusText.append("\nMust select two locations before making a path between them");
				}
				dispose();
			}else if(e.getActionCommand().equals("Remove Edge")){
				if (start != null && end != null && between != null){
					model.removeEdge(between);
				}else{
					statusText.append("\nCould not remove node");
				}
				dispose();
			}
		}

		public void centerOKButtonAt(int x, int y){
			Component toCenter;
			if(between != null){
				toCenter = cancel;
			}else{
				toCenter = ok;
			}
			Point coordinates1 = toCenter.getLocation();
			Point coordinates2 = toCenter.getParent().getLocation();
			int xDiff = coordinates1.x+coordinates2.x;
			int yDiff = coordinates1.y+coordinates2.y;
			//System.out.println(cordinates1);
			//System.out.println(ok.getLocationOnScreen());
			setLocation(x-xDiff-toCenter.getWidth()/2,y-yDiff-toCenter.getHeight()/2);
		}
	}

	/** Handles Radio Buttons to change the mode of the view between editing the map and finding paths*/
	//http://java.sun.com/docs/books/tutorial/uiswing/components/button.html#radiobutton
	class MapRadioButtons extends JPanel implements ActionListener{
		private JRadioButton add;
		private JRadioButton add2;
		private JRadioButton edit;
		private JRadioButton remove;
		private JRadioButton moveNode;
		private JLabel pathTypeslabel;
		private JComboBox pathTypes;
		private JCheckBox bidi;
		private JLabel blank;
		//private JRadioButton pathfind;
		//private JRadioButton randomPathAlgorithm;
		//private JRadioButton smartPathAlgorithm;
		//private JRadioButton averagePathAlgorithm;

		/**Creates the radio buttons we need*/
		public MapRadioButtons(){
			setLayout(new GridLayout(3,2));

			edit = new JRadioButton("Add/Edit Path");
			edit.setSelected(true);
			add = new JRadioButton("Add Node");
			add.setSelected(false);
			add2 = new JRadioButton("Add Building");
			add2.setSelected(false);
			remove = new JRadioButton("Remove Node");
			remove.setSelected(false);
			moveNode = new JRadioButton("Move Node");
			moveNode.setSelected(false);
			pathTypeslabel = new JLabel("Path Defaults:");
			bidi = new JCheckBox("Bi-Directional?");
			bidi.setSelected(true);
			blank = new JLabel();

			pathTypes = new JComboBox();

			Iterator<PathType> iter = model.getPathTypes().iterator();
			while(iter.hasNext()){
				PathType cur = iter.next();
				pathTypes.addItem(cur);
				if(!iter.hasNext()){
					chosenPathType = cur;
					pathTypes.setSelectedItem(cur);
				}
			}
			chosenBiDi = true;

			//pathfind = new JRadioButton("Find Path");
			//pathfind.setSelected(false);

			ButtonGroup guiFunctions = new ButtonGroup();
			guiFunctions.add(add);
			guiFunctions.add(add2);
			guiFunctions.add(edit);
			guiFunctions.add(remove);
			guiFunctions.add(moveNode);
			//guiFunctions.add(pathfind);

			//randomPathAlgorithm = new JRadioButton("Dumb Path");
			//randomPathAlgorithm.setSelected(false);
			//randomPathAlgorithm.setEnabled(false);

			//averagePathAlgorithm = new JRadioButton("Average Path");
			//averagePathAlgorithm.setSelected(false);
			//averagePathAlgorithm.setEnabled(false);

			//smartPathAlgorithm = new JRadioButton("Smart Path");
			//smartPathAlgorithm.setSelected(true);

			//ButtonGroup algorithms = new ButtonGroup();
			//algorithms.add(randomPathAlgorithm);
			//algorithms.add(averagePathAlgorithm);
			//algorithms.add(smartPathAlgorithm);
			add.addActionListener(this);
			add2.addActionListener(this);
			edit.addActionListener(this);
			remove.addActionListener(this);
			moveNode.addActionListener(this);
			pathTypes.addActionListener(this);
			bidi.addActionListener(this);
			//pathfind.addActionListener(this);
			//randomPathAlgorithm.addActionListener(this);
			//averagePathAlgorithm.addActionListener(this);
			//smartPathAlgorithm.addActionListener(this);

			//add(randomPathAlgorithm);
			//add(pathfind);
			//add(averagePathAlgorithm);
			add(add);
			add(add2);
			add(edit);
			add(remove);
			add(moveNode);
			add(blank);
			add(pathTypeslabel);
			add(pathTypes);
			add(bidi);
			//add(smartPathAlgorithm);

		}

		/**Process radio button changes*/
		public void actionPerformed(ActionEvent e){
			if(e.getActionCommand().equals("Add/Edit Path")){
				availableButtons.setAction("Editing");
				statusText.append("\nClick anywhere on the map to add a destination, click two nodes to create a path between them.");
			} else if (e.getActionCommand().equals("Remove Node")){
				availableButtons.setAction("Removing");
			} else if (e.getActionCommand().equals("Add Node")){
				availableButtons.setAction("Adding Nodes");
			} else if (e.getActionCommand().equals("Add Building")){
				availableButtons.setAction("Adding Buildings");
			} else if (e.getActionCommand().equals("Move Node")){
				availableButtons.setAction("Move Node");
			} else if (e.getSource() instanceof JComboBox){
				JComboBox cb = (JComboBox)e.getSource();
				PathType pathType = (PathType)cb.getSelectedItem();
				chosenPathType = pathType;
			} else if (e.getSource() instanceof JCheckBox){
				JCheckBox bidi = (JCheckBox)e.getSource();
				chosenBiDi = bidi.isSelected();
			}
		}
	}

	/**Menu for the frame*/
	//http://java.sun.com/docs/books/tutorial/uiswing/components/example-1dot4/MenuLookDemo.java
	class MapMenuBar implements ActionListener{

		/**Create the menu for the frame*/
		public JMenuBar createMenuBar(){
			JMenuBar menuBar = new JMenuBar();
			JMenu file, zoom, display;
			JMenuItem currentItem;

			//Build the file menu.
			file = new JMenu("File");
			file.setMnemonic(KeyEvent.VK_F);
			menuBar.add(file);

			//currentItem = new JMenuItem("Save");
			//currentItem.addActionListener(this);
			//file.add(currentItem);

			currentItem = new JMenuItem("Exit");
			currentItem.addActionListener(this);
			file.add(currentItem);

			//Build the colors menu.
			display = new JMenu("Change Colors");
			display.setMnemonic(KeyEvent.VK_C);
			menuBar.add(display);

			currentItem = new JMenuItem("Node");
			currentItem.addActionListener(this);
			display.add(currentItem);

			currentItem = new JMenuItem("Paths");
			currentItem.addActionListener(this);
			display.add(currentItem);

			//currentItem = new JMenuItem("Visited Path");
			//currentItem.addActionListener(this);
			//display.add(currentItem);

			currentItem = new JMenuItem("Start Node");
			currentItem.addActionListener(this);
			display.add(currentItem);

			currentItem = new JMenuItem("End Node");
			currentItem.addActionListener(this);
			display.add(currentItem);

			return menuBar;
		}

		/**Process actions from the menu*/
		public void actionPerformed(ActionEvent e){
			if(e.getActionCommand().equals("Exit")){
				frame.dispose();
			} else if(e.getActionCommand().equals("Node")){
				Color selected = JColorChooser.showDialog(frame, "Choose a color for Nodes",view.getNodeColor());
				if (selected != null){
					view.setNodeColor(selected);
				}
			} else if(e.getActionCommand().equals("Paths")){
				Color selected = JColorChooser.showDialog(frame, "Choose a color for Paths",view.getEdgeColor());
				if (selected != null){
					view.setEdgeColor(selected);
				}
			} else if(e.getActionCommand().equals("Start Node")){
				Color selected = JColorChooser.showDialog(frame, "Choose a color for the Start Node",view.getStartNodeColor());
				if (selected != null){
					view.setStartNodeColor(selected);
				}
			} else if(e.getActionCommand().equals("End Node")){
				Color selected = JColorChooser.showDialog(frame, "Choose a color for the End Node",view.getEndNodeColor());
				if (selected != null){
					view.setEndNodeColor(selected);
				}
			}
		}
	}
}
<<<<<<< HEAD:distribully/src/main/distribully/view/StartGameButton.java
package distribully.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;

public class StartGameButton extends JMenuItem implements MouseListener {

	private static final long serialVersionUID = -3732106117892923335L;

	public StartGameButton() {
		this.setText("start game");
		this.addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		System.out.println("mand");
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
=======
package distribully.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;

public class StartGameButton extends JMenuItem{

	private static final long serialVersionUID = -3732106117892923335L;

	public StartGameButton() {
		this.setText("start game");

		this.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent actionEvent) {
		      System.out.println("click");
		    }
		});
	}

}
>>>>>>> 2d426a26da268a1bc4a3ccfe4382631147e7e361:distribully/src/distribully/view/StartGameButton.java

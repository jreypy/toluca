/*
 * @(#)ChatArea.java
 *
 * Copyright (c) 2002, Jang-Ho Hwang
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 	1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 	2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 	3. Neither the name of the Jang-Ho Hwang nor the names of its contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *    $Id: ChatArea.java,v 1.2 2005/01/17 15:16:08 afeltes Exp $
 */
package rath.jmsn.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.rtf.RTFEditorKit;

import rath.jmsn.util.Emoticon;
/**
 * 채팅 대화창 컴포넌트
 * <p>
 * Special thank to pistos (Min-Jong Kim)
 *
 * @author Jangho Hwang, rath@linuxkorea.co.kr
 * @version $Id: ChatArea.java,v 1.2 2005/01/17 15:16:08 afeltes Exp $
 */
public abstract class ChatArea extends JTextPane implements ActionListener, MouseListener, MouseMotionListener
{
	private Emoticon emoticon = Emoticon.getInstance();
	private SimpleAttributeSet style;
	private Vector startIndex = new Vector();
	private Vector endIndex = new Vector();
	private Vector linkURL = new Vector();
	private JTextArea tempArea = new JTextArea();
	private boolean isViewEmoticon = true;//MainFrame.LOCALCOPY.getPropertyBoolean(MainFrame.EMOTICON_DISPLAY, true);
	private boolean useFixedFont = false;//MainFrame.LOCALCOPY.getPropertyBoolean(MainFrame.USE_FIXED_CHAT_FONT, false);

	public ChatArea()
	{
		style = new SimpleAttributeSet();
		StyleConstants.setForeground(style, Color.black);
		StyleConstants.setFontSize(style, 12);
		StyleConstants.setBold(style, false);
		StyleConstants.setItalic(style, false);
		this.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_C, 2), WHEN_FOCUSED );
		this.setContentType("text/rtf");
		this.setEditorKit(new RTFEditorKit());
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setEditable(false);
		setBorder( BorderFactory.createEmptyBorder(5,5,5,5) );
		new DropTarget( this, new DropListener() );
	}

	/**
	 * 드래그 앤 드롭을 통해 input된 java.io.File 객체들의 List를 넘여준다.
	 *
	 * @param files DND를 통해 도착한 파일객체들의 리스트
	 */
	public abstract void acceptFiles( List files );

	/**
	 * DND를 통해 BuddyTree로부터 친구를 초대했음을 알려준다.
	 *
	 * @param loginName 초대하고자하는 친구녀석의 이름
	 */
	public abstract void inviteFriend( String loginName );
		
	/**
	 * Message를 받아 표시해준다.
	 *
	 * @param msg 표시할 message
	 */
	public void append(String msg)
	{
		append(msg, new Color(0,0,0), "", "Dialog");
	}

	/**
	 * Message를 받아 표시해준다.
	 *
	 * @param msg 표시할 message
     * @param co  표시할 글자색
     * @param ef  표시할 글꼴타잎
 	 * @param fn  표시할 글꼴이름
	 */
	public void append(String msg, Color co, String ef, String fn)
	{

		Color a3 = StyleConstants.getForeground(style);
		StyleConstants.setForeground(style, co);

		String a2 = StyleConstants.getFontFamily(style);
		StyleConstants.setFontFamily(style, useFixedFont ? "Dialog" : fn );

		boolean fi = false;
		boolean fu = false;
		boolean fb = false;
		boolean fs = false;
		if(ef.indexOf('B')!=-1)
        {
			fb = StyleConstants.isBold(style);
			StyleConstants.setBold(style, true);
		}
		if(ef.indexOf('I')!=-1)
        {
			fi = StyleConstants.isItalic(style);
			StyleConstants.setItalic(style, true);
		}
		if(ef.indexOf('S')!=-1)
        {
			fs = StyleConstants.isStrikeThrough(style);
			StyleConstants.setStrikeThrough(style, true);
		}
		if(ef.indexOf('U')!=-1)
        {
			fu = StyleConstants.isUnderline(style);
			StyleConstants.setUnderline(style, true);
		}

		try
		{
			String s = this.getDocument().getText(0,getDocument().getLength());
			int d1 = s.length();
			this.getDocument().insertString(this.getDocument().getLength(), msg, style);
			this.setEditable(true);
			if( isViewEmoticon )
				replaceEmoticon(msg, d1);
			replaceURL(msg, d1);
			this.setEditable(false);
			// Temporary patch code.
			this.getDocument().insertString(this.getDocument().getLength(), "", style);
			setCaretPosition(getDocument().getLength());
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		StyleConstants.setFontFamily(style, a2);
		StyleConstants.setForeground(style, a3);

		StyleConstants.setBold(style, fb);
		StyleConstants.setItalic(style, fi);
		StyleConstants.setStrikeThrough(style, fs);
		StyleConstants.setUnderline(style, fu);
	}

	/**
	 * Message에서 이모티콘을 치환한다.
	 *
	 */
	protected void replaceEmoticon(String msg, int d1)
	{
		try
		{
			boolean hasMoreEmoticons = true;
			int lid = -1;
			while(hasMoreEmoticons)
			{
				Iterator it = emoticon.getEmoticons();
//			    Enumeration en = emoticon.getEmoticons();
				boolean a = false;

				while(it.hasNext())
//				while(en.hasMoreElements())
				{
					String key = it.next().toString();
//				    String key = en.nextElement().toString();
					int index = -1;

					if((index=msg.indexOf(key.toUpperCase(),lid))!=-1 || (index=msg.indexOf(key.toLowerCase(),lid))!=-1){

						lid = index;
						this.getDocument().remove( (d1+index), key.length() );
						this.setCaretPosition(d1+index);
						insertIcon(emoticon.get(key));
						msg = this.getDocument().getText(d1, getDocument().getLength()-d1);
						a = true;
						break;
					}
				}
				hasMoreEmoticons = a;
			}
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Message에서 URL을 치환한다.
	 *
	 */
	protected void replaceURL(String msg, int d1)
	{
		try
		{
			Document doc = this.getDocument();
			int lid = -1;
			boolean a1 = StyleConstants.isUnderline(style);
			boolean a2 = StyleConstants.isItalic(style);
			Color a3 = StyleConstants.getForeground(style);
			StyleConstants.setUnderline(style, true);
			StyleConstants.setForeground(style, Color.blue);
			while(true)
			{
				int index= -1;
				msg = doc.getText(d1, doc.getLength()-d1);
				if((index = msg.indexOf("http://", lid+1))!=-1)
				{
					lid = index;

					int endPoint=msg.indexOf(" ",lid+1);
					int te1 = msg.indexOf("http://", lid+1);
					int te2 = msg.indexOf("\n", lid+1);

					//System.out.println(te1+","+te2+","+endPoint);
					if(te1!=-1)
					{
						endPoint = Math.min(te1, endPoint);
						if(endPoint == -1)
							endPoint = te1;
					}
					if(te2!=-1)
					{
						endPoint = Math.min(te2, endPoint);
						if(endPoint == -1)
							endPoint = te2;
					}

					if(endPoint == -1)
						endPoint = msg.length()-1;

					String anchor = doc.getText(d1+lid, endPoint-lid);
					doc.remove(d1+lid, endPoint-lid);
					doc.insertString(d1+lid, anchor, style);
					doc.insertString(d1+lid+anchor.length(), " ", new SimpleAttributeSet());

					startIndex.add(new Integer(d1+lid));
					endIndex.add(new Integer(d1+endPoint));
					linkURL.add(anchor);
				}
                else
					break;
			}
			StyleConstants.setUnderline(style, a1);
			StyleConstants.setForeground(style, a3);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Ctrl+C 가 제대로 먹히지 않는 관계로 직접구현
	 * 
	 */
	public void actionPerformed(ActionEvent e)
	{
		tempArea.setText(this.getSelectedText());
		tempArea.selectAll();
		tempArea.copy();
	}

	/**
	 * URL을 클릭하면 해당 URL로 바로가기
	 *
	 * @param msg 표시할 message
	 */
	public void mouseClicked(MouseEvent e)
	{
		int idx = viewToModel(e.getPoint());
		for(int i=0;i<startIndex.size();i++)
		{
			int sp = ((Integer)startIndex.elementAt(i)).intValue();
			int ep = ((Integer)endIndex.elementAt(i)).intValue();
			if(idx>=sp & idx<=ep)
			{
				String url = (String)linkURL.elementAt(i);
				//System.out.println(url);
				showURL( url );
				break;
			}
		}
		
	}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}

	private void showURL( String urls )
	{
		try
		{
			String[] cmd = new String[2];
			/*
			 * currently support only win32 and netscape navigator.
			 * the browser to open will change to customize.
			 */
			cmd[0] = System.getProperty("os.name").startsWith("Windows") ?
				"explorer" : "mozilla";
			cmd[1] = "\"" + urls + "\"";
			Runtime.getRuntime().exec( cmd );
		}
		catch( Exception e )
		{
			System.err.println( e );
		}
	}

	/**
	 * URL위에 마우스가 올라가면 커서모양 바꾸기
	 *
	 * @param msg 표시할 message
	 */
	public void mouseMoved(MouseEvent e)
	{
		int idx = viewToModel(e.getPoint());
		int a =0;
		for(int i=0;i<startIndex.size();i++)
		{
			int sp = ((Integer)startIndex.elementAt(i)).intValue();
			int ep = ((Integer)endIndex.elementAt(i)).intValue();
			if(idx>=sp & idx<=ep)
			{
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				a++;
				break;
			}
		}
		if(a==0)
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	public void mouseDragged(MouseEvent e){}

    /**
     * 친구목록에서 Drag and drop하였을때, Invite action을 취할 수 있도록
     * Drop event를 수신하는 event listener 클래스이다.
     */
	private class DropListener implements DropTargetListener
	{
		public void dragEnter( DropTargetDragEvent e ) {}
		public void dragExit( DropTargetEvent e ) {}
		public void dragOver( DropTargetDragEvent e ) {}
		public void dropActionChanged( DropTargetDragEvent e ) {}
		public void drop( DropTargetDropEvent event )
		{
			try
			{
				Transferable t = event.getTransferable();
				if( t.isDataFlavorSupported(DataFlavor.javaFileListFlavor) )
				{
					event.acceptDrop( DnDConstants.ACTION_MOVE );
					List filelist = (List)t.getTransferData( DataFlavor.javaFileListFlavor );
					event.getDropTargetContext().dropComplete( true );

					acceptFiles( filelist );
				}
				else
				if( t.isDataFlavorSupported(DataFlavor.stringFlavor) )
				{
				    event.acceptDrop( DnDConstants.ACTION_MOVE );
					String loginName = (String)t.getTransferData( DataFlavor.stringFlavor );
					event.getDropTargetContext().dropComplete( true );

					inviteFriend( loginName );
				}
				else
					event.rejectDrop();
			}
			catch( IOException e )
			{
				event.rejectDrop();
			}
			catch( UnsupportedFlavorException e )
			{
				event.rejectDrop();
			}
		}
	}

	public void dispose()
	{
		this.setText( "" );
		startIndex.removeAllElements();
		endIndex.removeAllElements();
		linkURL.removeAllElements();
		tempArea.setText( "" );
		startIndex = null;
		endIndex = null;
		linkURL = null;
		tempArea = null;
	}
};

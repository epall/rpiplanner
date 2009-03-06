/*
 * Copyright (C) 2008 Eric Allen allene2@rpi.edu
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package com.swtdesigner;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ImageIcon;

public class SwingResourceManager {
	
	/**
	 * Maps image names to images
	 */
	private static HashMap<String, Image> m_ClassImageMap = new HashMap<String, Image>();
	
    /**
     * Returns an image encoded by the specified input stream
     * @param is InputStream The input stream encoding the image data
     * @return Image The image encoded by the specified input stream
     */
	private static Image getImage(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte buf[] = new byte[1024 * 4];
			while (true) {
				int n = is.read(buf);
				if (n == -1)
					break;
				baos.write(buf, 0, n);
			}
			baos.close();
			return Toolkit.getDefaultToolkit().createImage(baos.toByteArray());
		} catch (Throwable e) {
			return null;
		}
	}
	
    /**
     * Returns an image stored in the file at the specified path relative to the specified class
     * @param clazz Class The class relative to which to find the image
     * @param path String The path to the image file
     * @return Image The image stored in the file at the specified path
     */
	public static Image getImage(Class<?> clazz, String path) {
		String key = clazz.getName() + '|' + path;
		Image image = m_ClassImageMap.get(key);
		if (image == null) {
			if ((path.length() > 0) && (path.charAt(0) == '/')) {
				String newPath = path.substring(1, path.length());
				image = getImage(new BufferedInputStream(clazz.getClassLoader().getResourceAsStream(newPath)));
			} else {
				image = getImage(clazz.getResourceAsStream(path));
			}
			m_ClassImageMap.put(key, image);
		}
		return image;
	}
	
    /**
     * Returns an image stored in the file at the specified path
     * @param path String The path to the image file
     * @return Image The image stored in the file at the specified path
     */
	public static Image getImage(String path) {
		return getImage("default", path); //$NON-NLS-1$
	}
	
    /**
     * Returns an image stored in the file at the specified path
     * @param section String The storage section in the cache
     * @param path String The path to the image file
     * @return Image The image stored in the file at the specified path
     */
	public static Image getImage(String section, String path) {
		String key = section + '|' + SwingResourceManager.class.getName() + '|' + path;
		Image image = m_ClassImageMap.get(key);
		if (image == null) {
			try {
				FileInputStream fis = new FileInputStream(path);
				image = getImage(fis);
				m_ClassImageMap.put(key, image);
				fis.close();
			} catch (IOException e) {
				return null;
			}
		}
		return image;
	}
	
    /**
	 * Clear cached images in specified section
	 * @param section the section do clear
	 */
	public static void clearImages(String section) {
		for (Iterator<String> I = m_ClassImageMap.keySet().iterator(); I.hasNext();) {
			String key = I.next();
			if (!key.startsWith(section + '|'))
				continue;
			Image image = m_ClassImageMap.get(key);
			image.flush();
			I.remove();
		}
	}
	
    /**
     * Returns an icon stored in the file at the specified path relative to the specified class
     * @param clazz Class The class relative to which to find the icon
     * @param path String The path to the icon file
     * @return Icon The icon stored in the file at the specified path
     */
	public static ImageIcon getIcon(Class<?> clazz, String path) {
		return getIcon(getImage(clazz, path));
	}
	
    /**
     * Returns an icon stored in the file at the specified path
     * @param path String The path to the icon file
     * @return Icon The icon stored in the file at the specified path
     */
	public static ImageIcon getIcon(String path) {
		return getIcon("default", path); //$NON-NLS-1$
	}
	
    /**
     * Returns an icon stored in the file at the specified path
     * @param section String The storage section in the cache
     * @param path String The path to the icon file
     * @return Icon The icon stored in the file at the specified path
     */
	public static ImageIcon getIcon(String section, String path) {
		return getIcon(getImage(section, path));
	}

    /**
     * Returns an icon based on the specified image
     * @param image Image The original image
     * @return Icon The icon based on the image
     */
	public static ImageIcon getIcon(Image image) {
		if (image == null)
			return null;
		return new ImageIcon(image);
	}
}
/**
 * 
 */
package cts.qea.automation.utils;

/**********************************************************************
* COGNIZANT CONFIDENTIAL OR TRADE SECRET
*
* Copyright 2020 - 2023 Cognizant.  All rights reserved.
*
* NOTICE:  This unpublished material is proprietary to Cognizant and 
* its suppliers, if any.  The methods, techniques and technical 
* concepts herein are considered Cognizant confidential or trade 
* secret information.  This material may also be covered by U.S. or
* foreign patents or patent applications.  Use, distribution or 
* copying of these materials, in whole or in part, is forbidden, 
* except by express written permission of Cognizant.
***********************************************************************/

import java.awt.event.KeyEvent;

/**
 * @author aselvan
 *
 */
public class RobotHelper {

	public static int getKeyEvent(Character character) {
		int key = 0;
		Character ch = Character.toLowerCase(character);
		switch (ch) {
        case 'a': key = (KeyEvent.VK_A); break;
        case 'b': key = (KeyEvent.VK_B); break;
        case 'c': key = (KeyEvent.VK_C); break;
        case 'd': key = (KeyEvent.VK_D); break;
        case 'e': key = (KeyEvent.VK_E); break;
        case 'f': key = (KeyEvent.VK_F); break;
        case 'g': key = (KeyEvent.VK_G); break;
        case 'h': key = (KeyEvent.VK_H); break;
        case 'i': key = (KeyEvent.VK_I); break;
        case 'j': key = (KeyEvent.VK_J); break;
        case 'k': key = (KeyEvent.VK_K); break;
        case 'l': key = (KeyEvent.VK_L); break;
        case 'm': key = (KeyEvent.VK_M); break;
        case 'n': key = (KeyEvent.VK_N); break;
        case 'o': key = (KeyEvent.VK_O); break;
        case 'p': key = (KeyEvent.VK_P); break;
        case 'q': key = (KeyEvent.VK_Q); break;
        case 'r': key = (KeyEvent.VK_R); break;
        case 's': key = (KeyEvent.VK_S); break;
        case 't': key = (KeyEvent.VK_T); break;
        case 'u': key = (KeyEvent.VK_U); break;
        case 'v': key = (KeyEvent.VK_V); break;
        case 'w': key = (KeyEvent.VK_W); break;
        case 'x': key = (KeyEvent.VK_X); break;
        case 'y': key = (KeyEvent.VK_Y); break;
        case 'z': key = (KeyEvent.VK_Z); break;
        case '`': key = (KeyEvent.VK_BACK_QUOTE); break;
        case '0': key = (KeyEvent.VK_0); break;
        case '1': key = (KeyEvent.VK_1); break;
        case '2': key = (KeyEvent.VK_2); break;
        case '3': key = (KeyEvent.VK_3); break;
        case '4': key = (KeyEvent.VK_4); break;
        case '5': key = (KeyEvent.VK_5); break;
        case '6': key = (KeyEvent.VK_6); break;
        case '7': key = (KeyEvent.VK_7); break;
        case '8': key = (KeyEvent.VK_8); break;
        case '9': key = (KeyEvent.VK_9); break;
        case '-': key = (KeyEvent.VK_MINUS); break;
        case '=': key = (KeyEvent.VK_EQUALS); break;
        case '!': key = (KeyEvent.VK_EXCLAMATION_MARK); break;
        case '@': key = (KeyEvent.VK_AT); break;
        case '#': key = (KeyEvent.VK_NUMBER_SIGN); break;
        case '$': key = (KeyEvent.VK_DOLLAR); break;
        case '^': key = (KeyEvent.VK_CIRCUMFLEX); break;
        case '&': key = (KeyEvent.VK_AMPERSAND); break;
        case '*': key = (KeyEvent.VK_ASTERISK); break;
        case '(': key = (KeyEvent.VK_LEFT_PARENTHESIS); break;
        case ')': key = (KeyEvent.VK_RIGHT_PARENTHESIS); break;
        case '_': key = (KeyEvent.VK_MINUS); break;
        case '+': key = (KeyEvent.VK_PLUS); break;
        case '\t': key = (KeyEvent.VK_TAB); break;
        case '\n': key = (KeyEvent.VK_ENTER); break;
        case '[': key = (KeyEvent.VK_OPEN_BRACKET); break;
        case ']': key = (KeyEvent.VK_CLOSE_BRACKET); break;
        case '\\': key = (KeyEvent.VK_BACK_SLASH); break;
        case ';': key = (KeyEvent.VK_SEMICOLON); break;
        case ':': key = (KeyEvent.VK_COLON); break;
        case '\'': key = (KeyEvent.VK_QUOTE); break;
        case '"': key = (KeyEvent.VK_QUOTEDBL); break;
        case ',': key = (KeyEvent.VK_COMMA); break;
        case '<': key = (KeyEvent.VK_LESS); break;
        case '.': key = (KeyEvent.VK_PERIOD); break;
        case '>': key = (KeyEvent.VK_GREATER); break;
        case '/': key = (KeyEvent.VK_SLASH); break;
        case ' ': key = (KeyEvent.VK_SPACE); break;
        case '~':key = (KeyEvent.VK_SHIFT); break;
        case '|':key = (KeyEvent.VK_CONTROL); break;
        case '?':key = (KeyEvent.VK_ALT); break;
		}
		return key;
	}
	}

package com.biit.liferay.access;

import java.util.Enumeration;
import java.util.Hashtable;

import com.liferay.portal.model.Contact;

public class ContactPool {
	private final static long EXPIRATION_TIME = 300000;// 5 minutes

	private Hashtable<Long, Long> time; // Contact id -> time.
	private Hashtable<Long, Contact> contacts; // Contact id -> User.

	private static ContactPool instance = new ContactPool();

	public static ContactPool getInstance() {
		return instance;
	}

	private ContactPool() {
		time = new Hashtable<Long, Long>();
		contacts = new Hashtable<Long, Contact>();
	}

	public Contact getContact(long contactId) {
		long now = System.currentTimeMillis();
		Long storedObject = null;
		if (time.size() > 0) {
			Enumeration<Long> e = time.keys();
			while (e.hasMoreElements()) {
				storedObject = e.nextElement();
				if ((now - time.get(storedObject)) > EXPIRATION_TIME) {
					// object has expired
					removeContact(storedObject);
					storedObject = null;
				} else {
					if (contacts.get(storedObject) != null && contacts.get(storedObject).getContactId() == contactId) {
						return contacts.get(contactId);
					}
				}
			}
		}
		return null;
	}

	public void addContact(Contact contact) {
		if (contact != null) {
			time.put(contact.getContactId(), System.currentTimeMillis());
			contacts.put(contact.getUserId(), contact);
		}
	}

	private void removeContact(long contactId) {
		time.remove(contactId);
		contacts.remove(contactId);
	}

}

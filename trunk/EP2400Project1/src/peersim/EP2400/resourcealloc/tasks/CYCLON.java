/*
 * Copyright (c) 2010 LCN, EE school, KTH
 *
 */

package peersim.EP2400.resourcealloc.tasks;

import java.util.ArrayList;
import java.util.List;

import peersim.cdsim.CDProtocol;
import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Linkable;
import peersim.core.Node;

/**
 * 
 * Template class for CYCLON implementation
 * 
 */
public class CYCLON implements CDProtocol, Linkable {

	public static class Entry {
		public Entry(Node node, int age) {
			this.node = node;
			this.age = age;

		}

		private Node node;

		public Node getNode() {
			return node;
		}

		public void setNode(Node node) {
			this.node = node;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}

		private int age;

	}

	private List<Entry> entries;

	private final int cacheSize;
	private final int shuffleLength;

	private ArrayList<Entry> sentList;

	private String prefix;
	/**
	 * Cache size.
	 * 
	 * @config
	 */
	private static final String PAR_CACHE = "cache_size";

	/**
	 * Shuffle Length.
	 * 
	 * @config
	 */
	private static final String PAR_SHUFFLE_LENGTH = "shuffle_length";

	// ====================== initialization ===============================
	// =====================================================================

	public CYCLON(String prefix) {
		this.prefix = prefix;
		this.cacheSize = Configuration.getInt(prefix + "." + PAR_CACHE);
		this.shuffleLength = Configuration.getInt(prefix + "."
				+ PAR_SHUFFLE_LENGTH);
		this.entries = new ArrayList<Entry>(cacheSize);
	}

	public CYCLON(String prefix, int cacheSize, int shuffleLength) {
		this.prefix = prefix;
		this.cacheSize = cacheSize;
		this.shuffleLength = shuffleLength;
		this.entries = new ArrayList<Entry>(cacheSize);
	}

	// ---------------------------------------------------------------------

	public Object clone() {

		CYCLON cyclon = new CYCLON(this.prefix, this.cacheSize,
				this.shuffleLength);
		return cyclon;
	}

	// ====================== Linkable implementation =====================
	// ====================================================================

	public Node getNeighbor(int i) {
		return entries.get(i).getNode();
	}

	// --------------------------------------------------------------------

	/** Might be less than cache size. */
	public int degree() {
		return entries.size();
	}

	// --------------------------------------------------------------------

	public boolean addNeighbor(Node node) {
		Entry a = new Entry(node, 0);
		return this.entries.add(a);

	}

	// --------------------------------------------------------------------

	public void pack() {
	}

	// --------------------------------------------------------------------

	public boolean contains(Node n) {

		for (int i = 0; i < entries.size(); i++) {

			if (entries.get(i).getNode().equals(n)) {
				return true;
			}
		}
		return false;
	}

	private void validate() {
		if (entries.size() > cacheSize) {
			System.out
					.println(" CYCLON constraint is invalid : Entry size is higher than cache size");
			System.out.println(" Terminating now");
		}

	}

	// ===================== CDProtocol implementations ===================
	// ====================================================================

	public void nextCycle(Node n, int protocolID) {
		validate();
		// TODO Implement your code for task 1.1 here
		if (degree() == 0)
			return;
		// Increment neighbour ages
		for (Entry neighbour : entries) {
			neighbour.setAge(neighbour.age + 1);
		}
		// Get the oldest neighbour
		Entry oldest = getOldest();
		// Get l-1 neighbours
		ArrayList<Entry> shuffleList = getShuffleList();
		if (!shuffleList.contains(oldest)) {
			shuffleList.remove(shuffleList.size() - 1);
		} else {
			shuffleList.remove(oldest);
		}
		// Replace Q's entry with a new entry of age 0 and with P's address
		shuffleList.add(new Entry(n, 0));
		CYCLON luckyPeer = (CYCLON) oldest.getNode().getProtocol(protocolID);
		sentList = shuffleList;
		// Send the updated subset to peer Q
		luckyPeer.updateNeighbours(shuffleList, true, this, n);
	}

	public void updateNeighbours(ArrayList<Entry> list, boolean active,
			CYCLON from, Node n) {
		if (active) {
			ArrayList<Entry> shuffleList = getShuffleList();
			from.updateNeighbours(shuffleList, false, this, n);
			entries.addAll(list);
			while (degree() > cacheSize) {
				entries.remove(0);
			}
		}
		// Receive from Q a subset of its entries.
		else {
			// Discard entries pointing at P and entries already contained in
			// P's cache.
			for (Entry entry : list) {
				if ((!contains(entry.getNode()))
						&& (!entry.getNode().equals(n))) {
					entries.add(entry);
				}
			}
			// Update P's cache to include all remaining entries, by firstly
			// using empty cahce slots, and secondly replacing entries among the
			// ones sent to Q.
			while (degree() > cacheSize) {
				if (!sentList.isEmpty()) {
					entries.remove(sentList.remove(0));
				} else {
					entries.remove(0);
				}
			}
			sentList = null;
		}
	}

	private Entry getOldest() {
		Entry oldest = new Entry(null, 0);
		for (Entry entry : entries) {
			if (oldest.getAge() <= entry.getAge()) {
				oldest = entry;
			}
		}
		return oldest;
	}

	private ArrayList<Entry> getShuffleList() {
		ArrayList<CYCLON.Entry> result = new ArrayList<CYCLON.Entry>();
		int num = 0;
		int length = shuffleLength;
		if (degree() < shuffleLength) {
			length = degree();
		}
		while (num < length) {
			Entry e = entries.get(CommonState.r.nextInt(entries.size()));
			result.add(e);
			num++;
		}
		return result;
	}

	@Override
	public void onKill() {
	}

}

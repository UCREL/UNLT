# UNLT: Urdu Natural Language Toolkit

## UNLT

### Folder: UNLT/Src/Com/unlptk/tokenizer

Contains Urdu Word Tokenizer (UNLT-WT)

Run the file Main.java, which split text into words. Furthermore, this invokes the constructor of UrduWordTokenizer class (which contains the main functionality of Urdu word tokenizer mentioned in the paper)

Contains Urdu Sentence Tokenizer (UNLT-ST)

Run the file UrduSentenceTokenizer.java, which split text into sentences.

### Folder: UNLPTK/Src/com/unlptk/driver

Contains Urdu POS Tagger Code (UNLT-POSTagger)

Consists of two files: (i) Main.java and (ii) UrduPOSTagger.java

The main.java file contains static void method where you have imported the package com.unlptk.postagger to make a call to UrduPOSTAgger constructor (contains actual implementation of UNLT-Urdu POS Tagger).

Note: In the Library_Projects under the TnTTagger directory: You need to add tnt tagger into the directory before you can run the Urdu POS tagger.

### Licence

UNLT toolkit is made available under the terms of the GNU General Public License v3.0: https://www.gnu.org/licenses/gpl-3.0.en.html

## Resources

### Folder: UNLT-POS folder

Contains Training/testing data as well as Errors of existing POS Taggers

### Folder: UNLT-ST folder

Contains training/testing data for word tokenizer

### Folder: UNLT-WT folder

Contains Urdu Morphemes as well as multi-word dictionaries, training data (1/2/3-Grams) and testing data which has been used in UNLT-WT word tokenizer.

### Licence

UNLT resources are made available under the terms of the Creative Commons Attribution 4.0 International License: https://creativecommons.org/licenses/by/4.0/

### Reference

Shafi, J., Iqbal, H.R., Nawab, R.M.A, and Rayson, P. 2021. UNLT- Urdu Natural Language Toolkit. Natural Language Engineering, -(-), pp.


Button Finder
=====

**Usage**

java -jar buttonfinder-1.0.jar <input_origin_file_path> <input_other_sample_file_path> [origin_element_id]

origin_element_id is optional. If it is not specified, "make-everything-ok-button" will be used

The executable JAR file is located in the bin directory. 

**Output**

The tool outputs all found candidate elements with concomitant similarity scores.
The one with the highest score above the predefined threshold 0.85 is considered as the target button and its CSS path is also logged.

You can find the output of sample runs in the _output.txt_ file.
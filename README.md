# Overview

*FileOptLogger* is an android application  to collect VFS traces with **Inotify Tools** from Facebook's files & cache directory, including File Creation, File Reading & Writing and File Deletion.

User's phone mush be rooted because *FileOptLogger* needs root permission to access Facebook's directory.

*FileOptLogger*  won't collect any user's private information and users can check the source codes in this Github repository.



# Function

*FileOptLogger*  includes two switches and one button to allow users to control the collection behavior.

"**Start Collecting**" switch is to create a thread to collect data traces, and  these data will be stored in `/sdcard/fblog` directory.

"**Move Data to DB **" switch is to parse trace files to DB format and stored in database. *FileOptLogger* creates a database in `/data/data/edu.cityu.fileoptlogger/databases` directory, and this database includes two tables(hot_trace_info & cold_trace_info table). hot_trace_info table will store normal data(without file deletion record) and cold_trace_info table will store fully completed data(with file deletion record). 

"**LOAD LATEST TRACE**" button is to show users with available fully completed data.



# How to Use

Before launching Facebook, users should switch on "**Start Collecting**" , and *FileOptLogger*  will monitor Facebook's cache and files directory. After closing Facebook, users can also switch off"**Start Collecting**".

With the data collection running, the trace files in `/sdcard/fblog` will occupy a large storage space; thus, users can switch on "**Move Data to DB**", and *FileOptLogger*  will transfer trace data from files to database. Then, *FileOptLogger*  will delete the trace files in order to release storage space. To use the function, please keep *FileOptLogger* on the screen.



# Contact

if you have any suggestion or problem, please feel free to contact riweipan@gmail.com.




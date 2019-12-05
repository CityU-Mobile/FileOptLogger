# Overview

*FileOptLogger* is an android application  to collect VFS traces with **Inotify Tools** from Facebook's files & cache directory, including File Creation, File Reading & Writing and File Deletion.

User's phone mush be rooted because *FileOptLogger* needs root permission to access Facebook's directory.

*FileOptLogger*  won't collect any user's private information and users can check the source codes in this Github repository.



# Function

*FileOptLogger*  includes two switches and one button to allow users to control the collection behavior.

"**Start Collecting**" switch is used to create a thread to collect data traces, and  these data will be stored in `/sdcard/fblog` directory.

"**Compress Trace Files**" switch is used to compress trace files in order to reduce storage space.



# How to Use

### Data Collection

1.  make sure switch "**Start Collecting**" is on, then *FileOptLogger*  will monitor Facebook's cache and files directory.
2. use Facebook as normal.
3. switch off "**Start Collecting**", and trace files will be stored in `/sdcard/fblog`.

### Data Compression

With the data collection running, the trace files in `/sdcard/fblog` will occupy a large storage space. To save storage space, users can

1. switch on "**Compress Trace Files**", and *FileOptLogger*  will compress trace files.

2. To use the function, please switch off "**Start Collecting**" and keep *FileOptLogger* on the screen.

   

# Contact

if you have any suggestion or problem, please feel free to contact riweipan@gmail.com.




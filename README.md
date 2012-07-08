# filelister		

## Description

This java application iterates through a directory structure starting at a given directory in the UI.
It provides special output for some details of mp3 files.

### Purpose:

It is ment to generate a list of mp3 files contained in a directory to keep the knowledge about titels being sampled from a audio CD into a folder.

## Ant Build

On project level execute:
	ant -buildfile build/build.xml -propertyfile build/build_FileLister.properties run
	
## Usage

java -classpath FileLister.jar de.jjprojects.filelister.FileLister

## Author

Joerg Juenger ( jj-projects, joerg@jj-projects.de ), JJ-Projects Joerg Juenger

## Licenses

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

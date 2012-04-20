 /* Copyright 2012 Kristofer Mitchell

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/

package util;

public enum EventType {
	UNTYPED, SWAP_STUDY, SWAP_CARD_VIEW, SWAP_CARD_CREATE_VIEW,
	STUDY_MODE_PICKED, STUDY_SHOW_ANSWER, STUDY_GOT_ANSWER, FILE_OPEN, 
	FILE_SAVE_AS, FILE_SAVE, FILE_NEW, MODEL_TITLE_CHANGE, FILE_EXIT,
	CARD_ADD_TO_STACK, STACK_TITLE_CHANGE, MODEL_STACK_CHANGE, 
	EDIT_CARD, CLEAR_EDIT, DELETE_CARD, CHECK_UPDATES
}

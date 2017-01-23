"# north3221SmartThings"

 Repo created to store smartthings smartapps and device handlers I find. Also to play with and create my own

 Index

 Kodi Manager (Smartapp) & Kodi Client (Device Handler) - Version 1.0
 
 Taken from <https://github.com/Toliver182/SmartThings-Kodi>

 I wanted to have some more flexibility here and it doesnt look like its in active development anymore, so I have added
 some extra states (shutdown and start up). It still uses the callbacks at the moment, I prefer this implementation to
 others I have seen, like scheduler on smartthings. I want a more immediate change to occur than wiat for a scheduler to
 see it.

 I've also added some custom attributes:
 
    attribute "currentPlayingType", "string"
    attribute "currentPlayingCategory", "enum", ["Movie", "TV Show", "Sports", "None", "Unknown"]
    attribute "currentPlayingName", "string"

    currentPlayingType: This is taken directly from Kodi 'type' on now playing metadata. So if its your library you are
    playing, its likely it will know its 'Movie' or 'TV show'. However in other cases it will be 'unknown' so I added...

    currentPlayingCategory: I have added some code to try and work out what is playing. It uses some logic in order to
    try and work it out. This is because I want to be able to trigger different lights etc from different playing types.

        Logic:
            1   -   Check the kodi 'type' if its set then it knows best, so 'movie' = 'Movie' and 'episode' = 'TV Show'
            2   -   Then if kodi type is 'unknown' then I try and work it out in this order (stop at first that matches):
                    i   Movie = 'Movie Label' check kodi label for any of the words in Movie label
                    ii  Sports = 'Sports Label' check kodi label for any of the words in Sports labels
                    iii TV Show = 'TV Show label' check kodi label for any of the words in TV Show labels
                    iv  Movie = 'Minimum Movie Runtime' check if the runtime is longer than min movie runtime
                    v   TV Show = Runtime is > 0 but less than minimum movie runtime
                    vi  Movie = a 'plot' has been added by kodi

   NB The above labels and runtime is exposed in a device manager preference. So you can update the lists to your
      own liking. Please **BE AWARE** that the default values on a preference do not really exist - see the <a href="http://docs.smartthings.com/en/latest/device-type-developers-guide/device-preferences.html#additional-notes">Smartthings Docs</a> so if you 
      update **ANY** of them then you have to update them all (you can update to the same thing obviously)
         
     currentPlayingName: Calculated current playing title

  With these attributes please also be aware that kodi tells the device handler that its changed (from the call backs
     you configure, again see Toliver182's write up on install) then the device handler makes a call to ask kodi what's
     playing, then works out what's it is as per above. So this may take some time (dependant on networks and traffic I
     guess a few ms to a couple of seconds). Therefore, don't trigger on a state change then expect to be able to read
     the attribute, it may not be updated yet, I think I can fix this as part of many improvements I want to make, but 
     for now be careful.
     I have got round this by adding the customer attribute as the trigger in my CoRE Poston- NB you need to turn on 
     'expert mode' to do that.

  Installation:
  Read Toliver182's install instructions or add to your IDE. I'll update better instructions later if people start using
  my version (obviously use the north3221 versions!).



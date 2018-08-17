# OpenLyght
An Open Source lighting software, to control your lights over DMX512

## About the project:

Openlyght was born from the idea of trasforming entry-level dmx console into something better, a semi-professional setup, ideal for run lights in mid-level shows.

Openlyght is based on two main ideas:
- **Versatility:** Openlyght by its own it's just an interface, a sort of kernel, that manages everything and offers some basic functions to the user. It's like an empty box that helps you build your own show, your own gui, as easy as writing on your keyboard. You can integrate more elaborate functions and effects thanks to plugins.  
Openlyght provides you 3 useful plugins: [DMXUtils](https://github.com/stranck/OpenLyght/tree/master/Plugins/DMXUtils) _(for reciving input from your console and sending the output to your lights)_,  [ButtonUtils](https://github.com/stranck/OpenLyght/tree/master/Plugins/ButtonsUtils) _(for getting the input from some buttons mounted on an Arduino. Useful for having some exec buttons and sequences)_ and [ColorEffect](https://github.com/stranck/OpenLyght/tree/master/Plugins/ColorEffect) _(A special effect made to work with the color of your lights)_.

- **Channels and channels modifiers:** Openlyght has 512 physical channels (A dmx universe that should be outputted to lights) and an infinite amount of virtual channels (That can be used internally by the software/showfile). You can add an infinite number of channels modifier to each channel. A channel modifier it's just an object that recives a dmx value in input and returns another elaborated value in output. 

Taking a cue from the grandMa's engines _(the top tier of the lighting consolle imho)_, OpenLyght offers professional solutions to create all the effects you need to make your shows unforgettable.

Make sure to subscribe on my [Telegram channel](https://t.me/Stranck) to receive all the poject's updates!

![A screenshot from my showfile](https://i.imgur.com/TGXK6ft.png)

### Demo videos:
- _\[24/12/2017]_ [First livestream. Some random music](https://www.facebook.com/stranck/videos/1588491717897907)
- _\[25/12/2017]_ [Debut of OpenLyght in a real show. Christmas conert. Mainly rock version of cartoon's opening](https://youtu.be/tTs-XiNoE8I)
- _\[25/12/2017]_ [A clip of the previous show. Rock version of Daitarn 3's opening](https://www.facebook.com/stranck/videos/1592069637540115)
- _\[10/02/2018]_ [New movingheads and laser in my rig. Carnival conert. Mainly rock version of cartoon's opening](https://youtu.be/w89VZXAX3Yk)
- _\[10/02/2018]_ [Some clips of the previous show](https://www.facebook.com/stranck/videos/1663682173712194)
- _\[21/04/2018]_ [Live stream with general EDM dj-set by Christian Jackson](https://www.facebook.com/stranck/videos/1711742565572821)
- _\[21/04/2018]_ [A clip from the previous live stream](https://twitter.com/LStranck/status/987823769644302336)
- _\[27/05/2018]_ [First outdoor show. Mainly rock version of cartoon's opening](https://youtu.be/JpoVJbuO4jA)
- _\[27/05/2018]_ [A clip of the previous show. Rock version of Dragonball's opening](https://www.facebook.com/stranck/videos/1749767935103617)
- _\[04/07/2018]_ [First show with a faze machine. Mainly rock version of cartoon's opening](https://youtu.be/9YO8UPDB3ck)
- _\[04/07/2018]_ [A clip of the previous show. Rock version of Dragonball's opening](https://www.facebook.com/44008514630/videos/10156384365914631)
- _\[04/07/2018]_ [A clip from the previous show. Bohemian Rhapsody - Queen.](https://www.facebook.com/CdACartoonBoyBand/videos/834316146762842/)
- _\[04/07/2018]_ [A clip from the previous show. Rock version of italian Pokémon's opening.](https://www.facebook.com/stranck/videos/1809841142429629)

###### Why openl**Y**ght?
Simple: there are a lot of projects of lighting softwares already called openl**I**ght lmao

#### Libraries used:

* org.json
* rxtx java
* conceptinetics

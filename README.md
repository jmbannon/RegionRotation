# RegionRotation
An API that uses multiple WorldGuard regions to rotate states of a single WorldGuard region.

## Our Message
We are strong advocates for fair, fun, and balanced Minecraft gameplay. We do not condone the current tactics employed by
commercial servers for the sole purpose of profit revenue such as selling over-powered items, pay to be 
unbanned, ridiculously high pricing of cosmetic items, etc. The list goes on.

We strive to build a suite of plugins that can be modified by anyone with hopes that it contributes to 
small, community-ran servers willing to open-source. These plugins will be used by the Project Zombie server 
which will eventually be available to everyone to deploy their own custom instance.

## Interested in Contributing?
Please fork our repository and send a pull request with a meaningful feature/patch/bug-fix. We are always looking for other developers and are willing to help junior developers with the development process.

### User Guide for API
Terms:
  BaseState = The net.projectzombie.crackshot_enhanced.net.projectzombie.main attraction, the region and all its AltStates.
  AltState  = The sub regions that the BaseState can change to. 
  StateController = API which makes all the server BaseStates accessable. 
To use the StateController use "StateController.instance().methodYouWant()" in a java environment. And that is it.
Things you can do with StateController: Save BaseStates to disc, get BaseStates for looking only, remove BaseStates, Rotate and Reset BaseStates, and possible more (See source for more methods). 

package bms.player.beatoraja.skin;

import bms.player.beatoraja.MainState;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * スキンイメージ
 * 
 * @author exch
 */
public class SkinImage extends SkinObject {
	
	/**
	 * イメージ
	 */
	private TextureRegion[][] image;

	private int timing;
	private int id = -1;

	private int imageid = -1;

	public SkinImage() {
		
	}

	public SkinImage(int imageid) {
		setImageID(imageid);
	}

	public SkinImage(TextureRegion[] image, int cycle) {
		setImage(image, cycle);
	}
		
	public SkinImage(TextureRegion[][] image, int cycle) {
		setImage(image, cycle);
	}
		
	public TextureRegion[] getImage() {
		return image[0];
	}

	public TextureRegion getImage(long time, MainState state) {
		return getImage(0 ,time, state);
	}

	public TextureRegion getImage(int value, long time, MainState state) {
		if(getImageID() != -1) {
			return state.getImage(getImageID());
		}
		if(getCycle() == 0) {
			return image[value][0];
		}
		if(timing != 0 && timing < 256) {
			if(state.getTimer()[timing] == Long.MIN_VALUE) {
				return image[value][0];
			}
			time -= state.getTimer()[timing];
		}
		if(time < 0) {
			return image[value][0];
		}

		final int index = (int) ((time / (getCycle()  / image[value].length))) % image[value].length;
//		System.out.println(index + " / " + image.length);
		return image[value][index];
	}
	
	public void setImage(TextureRegion[] image, int cycle) {
		this.image = new TextureRegion[1][];
		this.image[0] = image;
		setCycle(cycle);
	}

	public void setImage(TextureRegion[][] image, int cycle) {
		this.image = image;
		setCycle(cycle);
	}

	public int getTiming() {
		return timing;
	}

	public void setTiming(int timing) {
		this.timing = timing;
	}

	public void draw(SpriteBatch sprite, long time, MainState state) {
	    if(getImageID() != -1) {
            Rectangle r = this.getDestination(time, state);
            TextureRegion tr = state.getImage(getImageID());
            if (r != null && tr != null) {
                draw(sprite, tr, r.x, r.y, r.width, r.height, getColor(time,state),getAngle(time,state));
            }
        } else {
            if(image == null) {
                return;
            }
            int value = 0;
            if(id != -1) {
                value = state.getNumberValue(id);
            }
            if(value >= image.length) {
                value = 0;
            }
            if(value < 0 || image[value].length == 0) {
                return;
            }

            Rectangle r = this.getDestination(time, state);
            if (r != null) {
                if(value >= 0 && value < image.length) {
                    draw(sprite, getImage(value, time, state), r.x, r.y, r.width, r.height, getColor(time,state),getAngle(time,state));
                }
            }
        }
	}
	
	public void dispose() {
		if(image != null) {
			for(TextureRegion[] tr : image) {
				for(TextureRegion ctr : tr) {
					ctr.getTexture().dispose();
				}
			}
			image = null;
		}
	}

	public void setReferenceID(int id) {
		this.id = id;
	}
	
	public int getImageID() {
		return imageid;
	}

	public void setImageID(int imageid) {
		this.imageid = imageid;
	}
}
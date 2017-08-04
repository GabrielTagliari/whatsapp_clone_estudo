package whatsapp.cursoandroid.com.whatsapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import whatsapp.cursoandroid.com.whatsapp.fragment.ContatosFragment;
import whatsapp.cursoandroid.com.whatsapp.fragment.ConversasFragment;

/**
 * Created by gabriel on 7/18/17.
 */

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloTabs = {"CONVERSAS", "CONTATOS"};


    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new ConversasFragment();
                break;
            case 1:
                fragment = new ContatosFragment();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloTabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tituloTabs[position];
    }
}
